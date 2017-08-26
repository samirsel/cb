package app.Services;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import app.Validations.GdaxOrderBookException;
import app.models.DerivedOrder;
import app.models.Quote;
import app.models.QuoteRequestBody;

@Service
public class CryptoExchangeServiceImpl implements CryptoExchangeService {
  private static final String GDAX_ORDER_BOOK_LEVEL = "2";
  private static final String ORDER_BOOK_NOT_FOUND_MESSAGE_KEY =
      "app.Validations.GdaxOrderBookException.OrderBookNotFoundMessage";
  private static final String AMOUNT_TOO_BIG_MESSAGE_KEY=
      "app.Validations.GdaxOrderBookException.AmountTooBigMessage";

  @Autowired
  private CryptoExchangeAsyncRestApiService apiService;

  @Override
  public void getPriceQuote(DeferredResult<ResponseEntity<Quote>> deferredResult,
                             QuoteRequestBody quoteRequest) {

    // Issue a request for OrderBook <BaseCurrency-QuoteCurrency>
    final String productId = CryptoServiceUtils.getCurrencyPair(quoteRequest.getBase_currency(),
        quoteRequest.getQuote_currency());
    final CompletableFuture<DerivedOrder[]> orderBookFuture =
        apiService.getOrderBook(productId, GDAX_ORDER_BOOK_LEVEL, quoteRequest.getAction(), false);

    // Gdax might not support this orderBook but instead supports the reverse order book.
    final String reverseProductId = CryptoServiceUtils.getCurrencyPair(
        quoteRequest.getQuote_currency(), quoteRequest.getBase_currency());
    final CompletableFuture<DerivedOrder[]> reverseOrderBookFuture =
        apiService.getOrderBook(reverseProductId, GDAX_ORDER_BOOK_LEVEL, quoteRequest.getAction(),
            true);
    
    orderBookFuture.thenAccept(derivedOrders -> {
      final Quote quote = CryptoServiceUtils.CalculateQuoteFromOrders(quoteRequest, derivedOrders);
      if (quote == null) {
        deferredResult.setErrorResult(new GdaxOrderBookException(AMOUNT_TOO_BIG_MESSAGE_KEY));
      } else {
        deferredResult.setResult(new ResponseEntity<>(quote, HttpStatus.OK));
      }
    });

    reverseOrderBookFuture.thenAccept(derivedOrders -> {
      final Quote quote = CryptoServiceUtils.CalculateQuoteFromOrders(quoteRequest, derivedOrders);
      if (quote == null) {
        deferredResult.setErrorResult(new GdaxOrderBookException(AMOUNT_TOO_BIG_MESSAGE_KEY));
      } else {
        deferredResult.setResult(new ResponseEntity<>(quote, HttpStatus.OK));
      }
    });

    final CompletableFuture<DerivedOrder[]> exHandledOrderBookFuture =
        orderBookFuture.exceptionally(throwable -> null);
    final CompletableFuture<DerivedOrder[]> exHandledReverseOrderBookFuture =
        reverseOrderBookFuture.exceptionally(throwable -> null);

    exHandledOrderBookFuture.thenCombine(exHandledReverseOrderBookFuture,
        (derivedOrders, derivedOrders2) -> {
          if (derivedOrders == null && derivedOrders2 == null) {
            deferredResult.setErrorResult(new GdaxOrderBookException(ORDER_BOOK_NOT_FOUND_MESSAGE_KEY));
          }
          return null;
        }
    );
  }
}

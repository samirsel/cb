package app.Services;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

  /**
   * This service method generates the requests price quote and sets it in the passed Completable
   * future (deferredResult).
   * @param finalResultFuture This will hold the quoted price once the processing is complete.
   * @param quoteRequest The request received for a price quote.
   */
  @Override
  public void generatePriceQuote(CompletableFuture<ResponseEntity<Quote>> finalResultFuture,
                                 QuoteRequestBody quoteRequest) {

    // Issue a request for OrderBook <BaseCurrency-QuoteCurrency>.
    final String productId = CryptoServiceUtils.getCurrencyPair(quoteRequest.getBase_currency(),
        quoteRequest.getQuote_currency());
    final CompletableFuture<DerivedOrder[]> orderBookFuture =
        apiService.getOrderBook(productId, GDAX_ORDER_BOOK_LEVEL, quoteRequest.getAction(), false);

    // Gdax might not support this orderBook but instead supports the reverse order book.
    // Issue a request for the reverse OrderBook <QuoteCurrency-BaseCurrency>.
    final String reverseProductId = CryptoServiceUtils.getCurrencyPair(
        quoteRequest.getQuote_currency(), quoteRequest.getBase_currency());
    final CompletableFuture<DerivedOrder[]> reverseOrderBookFuture =
        apiService.getOrderBook(reverseProductId, GDAX_ORDER_BOOK_LEVEL, quoteRequest.getAction(),
            true);

    // If any of the request succeeds, we use the order book of that request
    setupSuccessCallback(orderBookFuture, quoteRequest, finalResultFuture);
    setupSuccessCallback(reverseOrderBookFuture, quoteRequest, finalResultFuture);
    // If both requests fail then we send back an error to the user.
    setupFailureCallback(orderBookFuture, reverseOrderBookFuture, finalResultFuture);
  }

  private void setupSuccessCallback(CompletableFuture<DerivedOrder[]> orderBookFuture,
                            QuoteRequestBody quoteRequest,
                            CompletableFuture<ResponseEntity<Quote>> finalResultFuture) {
    orderBookFuture.thenAccept(derivedOrders -> {
      final Quote quote = CryptoServiceUtils.CalculateQuoteFromOrders(quoteRequest, derivedOrders);
      if (quote == null) {
        finalResultFuture.completeExceptionally(
            new GdaxOrderBookException(AMOUNT_TOO_BIG_MESSAGE_KEY));
      } else {
        finalResultFuture.complete(new ResponseEntity<>(quote, HttpStatus.OK));
      }
    });
  }

  private void setupFailureCallback(CompletableFuture<DerivedOrder[]> orderBookFuture,
                            CompletableFuture<DerivedOrder[]> reverseOrderBookFuture,
                            CompletableFuture<ResponseEntity<Quote>> finalResultFuture) {
    final CompletableFuture<DerivedOrder[]> exHandledOrderBookFuture =
        orderBookFuture.exceptionally(throwable -> null);
    final CompletableFuture<DerivedOrder[]> exHandledReverseOrderBookFuture =
        reverseOrderBookFuture.exceptionally(throwable -> null);

    exHandledOrderBookFuture.thenCombine(exHandledReverseOrderBookFuture,
        (derivedOrders, derivedOrders2) -> {
          if (derivedOrders == null && derivedOrders2 == null) {
            finalResultFuture.completeExceptionally(
                new GdaxOrderBookException(ORDER_BOOK_NOT_FOUND_MESSAGE_KEY));
          }
          return null;
        }
    );
  }
}

package app.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import app.Validations.GdaxOrderBookException;
import app.models.DerivedOrder;
import app.models.OrderBook;
import app.models.Quote;
import app.models.QuoteRequestBody;

@Service
public class CryptoExchangeServiceImpl implements CryptoExchangeService {
  private static final String GDAX_ORDER_BOOK_LEVEL = "2";
  private static final String ORDER_BOOK_NOT_FOUND_MESSAGE_KEY =
      "app.Validations.GdaxOrderBookException.OrderBookNotFoundMessage";

  @Autowired
  private CryptoExchangeRestApiService mCryptoEchangeRestApiService;

  @Override
  public Quote getPriceQuote(QuoteRequestBody quoteRequest) {
    final double amountBaseCurrency = Double.valueOf(quoteRequest.getAmount());

    final DerivedOrder[] derivedOrders = getDerivedAsksOrBids(quoteRequest);
    //Todo:sselman:validateOrderBook(orderBook);

    final double price = CryptoServiceUtils.calculatePrice(amountBaseCurrency, derivedOrders);
    //Todo:sselman: Double check if you can convert strings to Doubles and work with Doubles.

    final double amountQuoteCurrency = amountBaseCurrency * price;
    return new Quote(String.valueOf(price), String.valueOf(amountQuoteCurrency),
        quoteRequest.getQuote_currency());
  }

  private DerivedOrder [] getDerivedAsksOrBids(QuoteRequestBody quoteRequest) {
    boolean isReverseOrderBook = false;

    // Try order-book BaseCurrency-QuoteCurrency
    final String productId = CryptoServiceUtils.getCurrencyPair(quoteRequest.getBase_currency(),
        quoteRequest.getQuote_currency());

    ResponseEntity<OrderBook> orderBookResponseEntity =
        mCryptoEchangeRestApiService.getOrderBook(productId, GDAX_ORDER_BOOK_LEVEL);

    if (!orderBookResponseEntity.getStatusCode().is2xxSuccessful()) {
      // This orderBook is not supported by Gdax. Try the reverse order book.
      final String reverseProductId = CryptoServiceUtils.getCurrencyPair(
          quoteRequest.getQuote_currency(), quoteRequest.getBase_currency());
      orderBookResponseEntity = mCryptoEchangeRestApiService.getOrderBook(reverseProductId,
          GDAX_ORDER_BOOK_LEVEL);
      if (!orderBookResponseEntity.getStatusCode().is2xxSuccessful()) {
        throw new GdaxOrderBookException(ORDER_BOOK_NOT_FOUND_MESSAGE_KEY);
      }
      isReverseOrderBook = true;
    }

    final OrderBook orderBook = orderBookResponseEntity.getBody();
    final Object[][] relevantOrders = CryptoServiceUtils.getBidsOrAsks(orderBook,
        quoteRequest.getAction(), isReverseOrderBook);
    return CryptoServiceUtils.generateDerivedAsksOrBids(relevantOrders, isReverseOrderBook);
  }
}

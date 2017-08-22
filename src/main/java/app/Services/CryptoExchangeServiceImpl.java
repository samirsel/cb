package app.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import app.Validations.GdaxOrderBookException;
import app.models.OrderBook;
import app.models.Quote;
import app.models.QuoteRequestBody;

@Service
public class CryptoExchangeServiceImpl implements CryptoExchangeService {
  private static String GDAX_ORDER_BOOK_LEVEL = "2";
  private static String BUY_REQUEST_CODE = "buy";
  private static String SELL_REQUEST_CODE = "sell";

  @Autowired
  private CryptoExchangeRestApiService mCryptoEchangeRestApiService;

  @Override
  public Quote getPriceQuote(QuoteRequestBody quoteRequest) {
    final double amountBaseCurrency = Double.valueOf(quoteRequest.getAmount());

    final Object[][] orders = getRelevantOrders(quoteRequest);
    //Todo:sselman:validateOrderBook(orderBook);

    final double price = CryptoServiceUtils.calculatePrice(amountBaseCurrency, orders);
    //Todo:sselman: Double check if you can convert strings to Doubles and work with Doubles.

    final double amountQuoteCurrency = amountBaseCurrency * price;
    return new Quote(String.valueOf(price), String.valueOf(amountQuoteCurrency),
        quoteRequest.getQuote_currency());
  }

  private Object[][] getRelevantOrders(QuoteRequestBody quoteRequest) {
    boolean isReverseOrderBook = false;

    // Try order-book BaseCurrency-QuoteCurrency
    final String productId = getCurrencyPair(quoteRequest.getBase_currency(),
        quoteRequest.getQuote_currency());

    ResponseEntity<OrderBook> orderBookResponseEntity =
        mCryptoEchangeRestApiService.getOrderBook(productId, GDAX_ORDER_BOOK_LEVEL);

    if (!orderBookResponseEntity.getStatusCode().is2xxSuccessful()) {
      // This orderBook is not supported by Gdax. Try the reverse order book.
      final String reverseProductId = getCurrencyPair(quoteRequest.getQuote_currency(),
          quoteRequest.getBase_currency());
      orderBookResponseEntity = mCryptoEchangeRestApiService.getOrderBook(reverseProductId,
          GDAX_ORDER_BOOK_LEVEL);
      if (!orderBookResponseEntity.getStatusCode().is2xxSuccessful()) {
        //Todo:sselman: Fix this
        throw new GdaxOrderBookException("The order book for the supplied currencies was not " +
            "found");
      }
      isReverseOrderBook = true;
    }

    final OrderBook orderBook = orderBookResponseEntity.getBody();
    final Object[][] orders =
        (quoteRequest.getAction().equalsIgnoreCase(BUY_REQUEST_CODE) && !isReverseOrderBook) ||
            (quoteRequest.getAction().equalsIgnoreCase(SELL_REQUEST_CODE) && isReverseOrderBook) ?
        orderBook.getAsks() : orderBook.getBids();
    return orders;
  }

  private String getCurrencyPair(String currency1, String currency2) {
    return currency1 + "-" + currency2;
  }
}

package app.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.Services.CryptoExchangeRestApiService;
import app.Services.CryptoServiceUtils;
import app.models.OrderBook;
import app.models.Quote;
import app.models.QuoteRequestBody;

@Service
public class CryptoExchangeServiceImpl implements CryptoExchangeService {
  private static String GDAX_ORDER_BOOK_LEVEL = "2";

  @Autowired
  private CryptoExchangeRestApiService mCryptoEchangeRestApiService;

  public Quote getPriceQuote(QuoteRequestBody quoteRequest) {
    final String productId = quoteRequest.getBase_currency() + "-" + quoteRequest.getQuote_currency();

    final OrderBook orderBook = mCryptoEchangeRestApiService.getOrderBook(productId, GDAX_ORDER_BOOK_LEVEL);
    //Todo:sselman:validateOrderBook(orderBook);

    final double price = CryptoServiceUtils.calculatePrice(quoteRequest, orderBook);
    //Todo:sselman: Double check if you can convert strings to Doubles and work with Doubles.

    final double amountQuoteCurrency = Double.valueOf(quoteRequest.getAmount()) * price;
    return new Quote(String.valueOf(price), String.valueOf(amountQuoteCurrency),
        quoteRequest.getQuote_currency());
  }
}

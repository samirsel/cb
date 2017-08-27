package app.Services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

import app.models.DerivedOrder;
import app.models.OrderBook;
@Service
public class CryptoEchangeAsyncRestApiServiceImpl implements CryptoExchangeAsyncRestApiService {
  private static String PRODUCT_ID_KEY = "productId";
  private static String LEVEL_KEY = "level";

  @Autowired
  @Value("${app.Services.CryptoEchangeAsyncRestApiServiceImpl.GdaxRestHost}")
  private String GDAX_REST_HOST;
  private final AsyncRestTemplate mAsyncRestTemplate;

  public CryptoEchangeAsyncRestApiServiceImpl() {
    mAsyncRestTemplate = new AsyncRestTemplate();
  }

  /**
   * Makes an asynchronous request to get the order book specified.
   * @param productId - The order book ID eq (BTC-USD)
   * @param level - level 1,2,3. Please see Gdax documentation.
   * @param userAction - Specifies the user's action in the request: "buy" or "sell"
   * @param isReverseOrderBook - Specifies if the reverse order book should be used eq. (USD-BTC)
   * @return A future holding an adapted list of DerivedOrders.
   */
  @Override
  public CompletableFuture<DerivedOrder[]> getOrderBook(
      String productId, String level, String userAction, boolean isReverseOrderBook) {
    final String requestSubPath = "/products/{productId}/book?level={level}";
    final String fullRequestUrl = GDAX_REST_HOST + requestSubPath;
    final Map<String, String> vars = new HashMap<>();
    vars.put(PRODUCT_ID_KEY, productId);
    vars.put(LEVEL_KEY, level);

    final ListenableFuture<ResponseEntity<OrderBook>> orderBooksFuture =
        mAsyncRestTemplate.getForEntity(fullRequestUrl, OrderBook.class, vars);

    final ListenableFuture<DerivedOrder[]> derivedOrderListenableFuture =
        new OrderBookAdapter(orderBooksFuture, userAction, isReverseOrderBook);

    return CryptoServiceUtils.buildCompletableFutureFromListenableFuture(
        derivedOrderListenableFuture);
  }
}

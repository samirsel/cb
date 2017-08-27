package app.Services;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import app.models.DerivedOrder;

@Service
public interface CryptoExchangeAsyncRestApiService {
  /**
   * Makes an asynchronous request to get the order book specified.
   * @param productId - The order book ID eq (BTC-USD)
   * @param level - level 1,2,3. Please see Gdax documentation.
   * @param userAction - Specifies the user's action in the request: "buy" or "sell"
   * @param isReverseOrderBook - Specifies if the reverse order book should be used eq. (USD-BTC)
   * @return A future holding an adapted list of DerivedOrders.
   */
  CompletableFuture<DerivedOrder[]> getOrderBook(
      String productId, String level, String userAction, boolean isReverseOrderBook);
}

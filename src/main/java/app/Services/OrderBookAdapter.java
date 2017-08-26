package app.Services;

import java.util.concurrent.ExecutionException;

import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureAdapter;

import app.models.DerivedOrder;
import app.models.OrderBook;

public class OrderBookAdapter extends ListenableFutureAdapter<DerivedOrder[],
    ResponseEntity<OrderBook>> {
  private final String userAction;
  private final boolean isReverseOrderBook;
  /**
   * Construct a new {@code ListenableFutureAdapter} with the given adaptee.
   *
   * @param adaptee the future to adapt to
   */
  protected OrderBookAdapter(ListenableFuture<ResponseEntity<OrderBook>> adaptee,
                             String userAction, boolean isReverseOrderBook) {
    super(adaptee);
    this.userAction = userAction;
    this.isReverseOrderBook = isReverseOrderBook;
  }

  @Override
  protected DerivedOrder[] adapt(ResponseEntity<OrderBook> adapteeResult) throws ExecutionException {
    final OrderBook orderBook = null;
    final Object[][] relevantOrders = CryptoServiceUtils.getBidsOrAsks(orderBook,
        userAction, isReverseOrderBook);
    return CryptoServiceUtils.generateDerivedAsksOrBids(relevantOrders, isReverseOrderBook);
  }
}

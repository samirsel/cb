package app.Services;

import java.util.concurrent.CompletableFuture;

import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import app.models.DerivedOrder;
import app.models.OrderBook;
import app.models.Quote;
import app.models.QuoteRequestBody;

public class CryptoServiceUtils {
  private static final String BUY_REQUEST_CODE = "buy";
  private static final String SELL_REQUEST_CODE = "sell";
  private static final int INDEX_PRICE = 0;
  private static final int INDEX_SIZE = 1;
  private static final int INDEX_NUM_ORDERS = 2;

  /**
   * Calculates the price quote given the set of relevant orders.
   * @param quoteRequest The request for a price quote
   * @param orders The list of relevant orders.
   * @return An instance of a Quote that contains the price quote information or null in case
   * the amount provided is too big.
   */
  public static Quote CalculateQuoteFromOrders(QuoteRequestBody quoteRequest,
                                                DerivedOrder[] orders) {
    final double amountBaseCurrency = Double.valueOf(quoteRequest.getAmount());
    double remainingAmount = amountBaseCurrency;
    Double weightedSum = 0d;
    for (int i=0; i< orders.length && remainingAmount > 0; i++) {
      final DerivedOrder order = orders[i];
      if (order.amount > remainingAmount) {
        weightedSum += remainingAmount * order.price;
        remainingAmount = 0;
      } else {
        weightedSum += order.amount * order.price;
        remainingAmount -= order.amount;
      }
    }
    if (remainingAmount > 0) {
      return null;
    }
    final double price = weightedSum / amountBaseCurrency;
    final double amountQuoteCurrency = amountBaseCurrency * price;
    return new Quote(String.valueOf(price), String.valueOf(amountQuoteCurrency),
        quoteRequest.getQuote_currency());
  }

  /**
   * Generates an array of DerivedOrders given an orderbook and whether it's the forward or
   * reverse orderbook.
   * @param orders The orderbook as retrieved from gdax.
   * @param isReverseOrderBook Whether this is the forward or reverse order book.
   * @return An array of DerivedOrders.
   */
  public static DerivedOrder[] generateDerivedAsksOrBids(Object[][] orders,
                                                         boolean isReverseOrderBook) {
    final DerivedOrder[] derivedOrders = new DerivedOrder[orders.length];
    for(int i=0; i< orders.length; i++) {
      final Object[] order = orders[i];
      final double orderSizeForwardBook = Double.valueOf((String) order[INDEX_SIZE]);
      final int numOrdersForwardBook = (Integer) order[INDEX_NUM_ORDERS];
      final double unitPriceForwardBook = Double.valueOf((String) order[INDEX_PRICE]);
      double finalTotalAmount;
      double finalUnitPrice;

      if (isReverseOrderBook) {
        // Eq: BTC-USD book - totalAmount = 2BTC * 2 orders * $4000/BTC = $16000
        finalTotalAmount = orderSizeForwardBook * numOrdersForwardBook * unitPriceForwardBook;
        finalUnitPrice = 1 / unitPriceForwardBook;
      } else {
        finalTotalAmount = orderSizeForwardBook * numOrdersForwardBook;
        finalUnitPrice = unitPriceForwardBook;
      }
      derivedOrders[i] = new DerivedOrder(finalTotalAmount, finalUnitPrice);
    }
    return derivedOrders;
  }

  public static Object[][] getBidsOrAsks(OrderBook orderBook, String action,
                                         boolean isReverseOrderBook) {
    return (action.equalsIgnoreCase(BUY_REQUEST_CODE) && !isReverseOrderBook) ||
        (action.equalsIgnoreCase(SELL_REQUEST_CODE) && isReverseOrderBook) ?
        orderBook.getAsks() : orderBook.getBids();
  }

  public static String getCurrencyPair(String currency1, String currency2) {
    return currency1 + "-" + currency2;
  }

  /**
   * Builds a CompletableFuture from a ListenableFuture. CompletableFutures offer a lot more
   * functionality such as combining multiple futures, applying functionaly transformations...
   *
   * @param listenableFuture The listenable future.
   * @return The corresponding CompletableFuture.
   */
  public static <T> CompletableFuture<T> buildCompletableFutureFromListenableFuture(
      final ListenableFuture<T> listenableFuture) {
    CompletableFuture<T> completableFuture = new CompletableFuture<T>() {
      @Override
      public boolean cancel(boolean mayInterruptIfRunning) {
        boolean result = listenableFuture.cancel(mayInterruptIfRunning);
        super.cancel(mayInterruptIfRunning);
        return result;
      }
    };
    listenableFuture.addCallback(new ListenableFutureCallback<T>() {
      @Override
      public void onSuccess(T result) {
        completableFuture.complete(result);
      }
      @Override
      public void onFailure(Throwable t) {
        completableFuture.completeExceptionally(t);
      }
    });
    return completableFuture;
  }
}

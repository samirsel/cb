package app.Services;

import app.Validations.GdaxOrderBookException;
import app.models.DerivedOrder;
import app.models.OrderBook;

public class CryptoServiceUtils {
  private static final String BUY_REQUEST_CODE = "buy";
  private static final String SELL_REQUEST_CODE = "sell";
  private static final int INDEX_PRICE = 0;
  private static final int INDEX_SIZE = 1;
  private static final int INDEX_NUM_ORDERS = 2;
  private static final String AMOUNT_TOO_BIG_MESSAGE_KEY=
      "app.Validations.GdaxOrderBookException.AmountTooBigMessage";

  public static double calculatePrice(double amount, DerivedOrder[] orders) {
    double remainingAmount = amount;
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
      throw new GdaxOrderBookException(AMOUNT_TOO_BIG_MESSAGE_KEY);
    }
    return weightedSum / amount;
  }

  public static DerivedOrder[] generateDerivedAsksOrBids(Object[][] orders,
                                                         boolean isReverseOrderBook) {
    final DerivedOrder[] derivedOrders = new DerivedOrder[orders.length];
    for(int i=0; i< orders.length; i++) {
      final Object[] order = orders[i];
      //Todo:sselman Null check these.
      final double orderSizeForwardBook = Double.valueOf((String) order[INDEX_SIZE]);
      final int numOrdersForwardBook = (Integer) order[INDEX_NUM_ORDERS];
      final double unitPriceForwardBook = Double.valueOf((String) order[INDEX_PRICE]);
      double finalTotalAmount;
      double finalUnitPrice;

      if (isReverseOrderBook) {
        //Eq: BTC-USD book - totalAmount = 2BTC * 2 orders * $4000/BTC = $16000
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
}

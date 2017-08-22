package app.Services;

import app.Validations.GdaxOrderBookException;

public class CryptoServiceUtils {
  private static int INDEX_PRICE = 0;
  private static int INDEX_SIZE = 1;
  private static int INDEX_NUM_ORDERS = 2;

  public static double calculatePrice(double amount, Object[][] orders) {
    double remainingAmount = amount;

    Double weightedSum = 0d;
    for (int i=0; i< orders.length && remainingAmount > 0; i++) {
      final Object[] order = orders[i];
      //Todo:sselman Null check these.
      final Double orderSize = Double.valueOf((String) order[INDEX_SIZE]);
      final Integer numOrders = (Integer) order[INDEX_NUM_ORDERS];
      final Double unitPrice = Double.valueOf((String) order[INDEX_PRICE]);

      // Orders are sorted on price (Asks ascending, Bids descending).
      final double availableSize = orderSize * numOrders;
      if (availableSize > remainingAmount) {
        weightedSum += remainingAmount * unitPrice;
        remainingAmount = 0;
      } else {
        weightedSum += availableSize * unitPrice;
        remainingAmount -= availableSize;
      }
    }
    if (remainingAmount > 0) {
      //Todo:sselman: How to handle case where amount greater than open orders?
      // Maybe throw error Amount too big.
      throw new GdaxOrderBookException("Add message here");
    }
    return weightedSum / amount;
  }
}

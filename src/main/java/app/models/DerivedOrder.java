package app.models;

/**
 * A DerivedOrder represents an aggregated order for a certain currency ratio.
 *
 * Example:
 * Given an order book with an ask for BTC [ price1, size1, num-orders ]
 *
 * The derived Order of a bid for USD would have DerivedOrder.amount = size1*num-orders*price1
 * and DerivedOrder.price = 1/price1.
 *
 * The derived Order of an ask for BSD would have DerivedOrder.amount = size1*num-orders and
 * DerivedOrder.price = price1.
 */
public class DerivedOrder {
  public double amount;
  public double price;
  public DerivedOrder(double amount, double price) {
    this.amount = amount;
    this.price = price;
  }
}

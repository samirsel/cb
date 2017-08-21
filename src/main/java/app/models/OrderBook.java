package app.models;

/* Example:
  "bids": [
    [ price, size, num-orders ],
    [ "295.96", "4.39088265", 2 ],
    ...
   ]
*/
public class OrderBook {
    private String sequence;
    private Object[][] bids;
    private Object[][] asks;

    public OrderBook() {
    }

    public String getSequence() {
        return sequence;
    }
    public Object[][] getBids() {
        return bids;
    }
    public Object[][] getAsks() {
        return asks;
    }
}

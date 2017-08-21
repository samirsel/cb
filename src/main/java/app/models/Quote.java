package app.models;

public class Quote {
    private final String mPrice;
    private final String mTotal;
    private final String mCurrency;

    public Quote(String price, String total, String currency) {
        mPrice = price;
        mTotal = total;
        mCurrency = currency;
    }

    public String getPrice() {
        return mPrice;
    }

    public String getTotal() {
        return mTotal;
    }

    public String getCurrency() {
        return mCurrency;
    }
}

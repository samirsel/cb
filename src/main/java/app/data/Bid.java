package app.data;

public class Bid {

    private final String mTotal;
    private final String mCurrency;
    private final String mBaseCurrency;

    public Bid() {
        mTotal = "0";
        mCurrency ="";
        mBaseCurrency ="";
    }
    public Bid(String baseCurrency, String total, String currency) {
        mBaseCurrency = baseCurrency;
        mTotal = total;
        mCurrency = currency;
    }

    public String getmBaseCurrency() {
        return mBaseCurrency;
    }

    public String getTotal() {
        return mTotal;
    }

    public String getCurrency() {
        return mCurrency;
    }
}
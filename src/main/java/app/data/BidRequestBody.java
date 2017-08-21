package app.data;

public class BidRequestBody {

    private final String total;
    private final String currency;
    private final String baseCurrency;

    public BidRequestBody() {
        total = "0";
        currency ="";
        baseCurrency ="";
    }
    public BidRequestBody(String baseCurrency, String total, String currency) {
        this.baseCurrency = baseCurrency;
        this.total = total;
        this.currency = currency;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getTotal() {
        return total;
    }

    public String getCurrency() {
        return currency;
    }
}
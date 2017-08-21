package app.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;

@Entity
public class BidEntity {
    @Id
    @GeneratedValue
    private Long id;

    private final String mTotal;
    private final String mCurrency;
    private final String mBaseCurrency;

    public BidEntity() {
        mTotal = "";
        mCurrency = "";
        mBaseCurrency = "";
    }

    public BidEntity(String baseCurrency, String total, String currency) {
        super();
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

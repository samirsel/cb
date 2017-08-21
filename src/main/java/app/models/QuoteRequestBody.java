package app.models;

import org.hibernate.validator.constraints.NotBlank;

import app.Validations.CheckStringVal;
import app.Validations.RangeExclude;

public class QuoteRequestBody {
    @NotBlank(message = "{org.hibernate.validator.constraints.NotBlank.ActionMessage}")
    @CheckStringVal(value = {"buy","sell"},
        message = "{app.Validations.CheckStringVal.ActionMessage}")
    private String action;

    @NotBlank(message = "{org.hibernate.validator.constraints.NotBlank.BaseCurrencyMessage}")
    @CheckStringVal(value = {"BTC","ETH", "LTC"},
        message = "{app.Validations.CheckStringVal.BaseCurrencyMessage}")
    private String base_currency;

    @NotBlank(message = "{org.hibernate.validator.constraints.NotBlank.QuoteCurrencyMessage}")
    @CheckStringVal(value = {"USD"},
        message = "{app.Validations.CheckStringVal.QuoteCurrencyMessage}")
    private String quote_currency;

    @NotBlank(message = "{org.hibernate.validator.constraints.NotBlank.AmountMessage}")
    @RangeExclude(value = {0}, message = "{app.Validations.RangeExclude.AmountMessage}")
    private String amount;

    public QuoteRequestBody() {
    }

    public String getAction() {
        return action;
    }

    public String getBase_currency() {
        return base_currency;
    }

    public String getAmount() {
        return amount;
    }

    public String getQuote_currency() {
        return quote_currency;
    }
}
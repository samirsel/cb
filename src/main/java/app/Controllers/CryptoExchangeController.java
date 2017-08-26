package app.Controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import app.Services.CryptoExchangeService;
import app.models.Quote;
import app.models.QuoteRequestBody;

@RestController
public class CryptoExchangeController {

  @Autowired
  private CryptoExchangeService mCryptoExchangeService;

  @RequestMapping(value= "/quote", method = RequestMethod.POST)
  public DeferredResult<ResponseEntity<Quote>> quote(
      @Valid @RequestBody QuoteRequestBody quoteRequestBody) {

    DeferredResult<ResponseEntity<Quote>> deferredResult = new DeferredResult<>();
    mCryptoExchangeService.getPriceQuote(deferredResult, quoteRequestBody);
    return deferredResult;
  }
}
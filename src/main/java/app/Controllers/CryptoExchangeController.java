package app.Controllers;

import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.Services.CryptoExchangeService;
import app.models.Quote;
import app.models.QuoteRequestBody;

@RestController
public class CryptoExchangeController {

  @Autowired
  private CryptoExchangeService mCryptoExchangeService;

  @RequestMapping(value= "/quote", method = RequestMethod.POST)
  public CompletableFuture<ResponseEntity<Quote>> quote(
      @Valid @RequestBody QuoteRequestBody quoteRequestBody) {

    CompletableFuture<ResponseEntity<Quote>> finalResultFuture = new CompletableFuture<>();
    mCryptoExchangeService.generatePriceQuote(finalResultFuture, quoteRequestBody);
    return finalResultFuture;
  }
}
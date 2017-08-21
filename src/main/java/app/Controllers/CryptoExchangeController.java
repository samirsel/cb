package app.Controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<Quote> quote(@Valid @RequestBody QuoteRequestBody quoteRequestBody) {
    //Todo:sselman: Handle invalid request bodies...
    final Quote quote = mCryptoExchangeService.getPriceQuote(quoteRequestBody);
    return new ResponseEntity<>(quote, HttpStatus.OK);
  }
}
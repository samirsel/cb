package app.Services;

import java.util.concurrent.CompletableFuture;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import app.models.Quote;
import app.models.QuoteRequestBody;

@Service
public interface CryptoExchangeService {
  /**
   * This service method generates the requests price quote and sets it in the passed Completable
   * future (deferredResult).
   * @param finalResultFuture This will hold the quoted price once the processing is complete.
   * @param quoteRequest The request received for a price quote.
   */
  void generatePriceQuote(CompletableFuture<ResponseEntity<Quote>> finalResultFuture,
                          QuoteRequestBody quoteRequest);
}

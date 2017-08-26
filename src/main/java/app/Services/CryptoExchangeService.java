package app.Services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import app.models.Quote;
import app.models.QuoteRequestBody;

@Service
public interface CryptoExchangeService {
  void getPriceQuote(DeferredResult<ResponseEntity<Quote>> deferredResult,
                      QuoteRequestBody quoteRequest);
}

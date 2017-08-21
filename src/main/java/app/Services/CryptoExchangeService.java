package app.Services;

import org.springframework.stereotype.Service;

import app.models.Quote;
import app.models.QuoteRequestBody;

@Service
public interface CryptoExchangeService {
  Quote getPriceQuote(QuoteRequestBody quoteRequest);
}

package app.Services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import app.models.OrderBook;

@Service
public interface CryptoExchangeRestApiService {
  ResponseEntity<OrderBook> getOrderBook(String productId, String level) throws RestClientException;
}

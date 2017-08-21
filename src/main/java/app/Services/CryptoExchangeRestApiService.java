package app.Services;

import org.springframework.stereotype.Service;

import app.models.OrderBook;

@Service
public interface CryptoExchangeRestApiService {
  OrderBook getOrderBook(String productId, String level);
}

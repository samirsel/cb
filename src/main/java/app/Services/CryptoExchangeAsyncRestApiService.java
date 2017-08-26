package app.Services;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import app.models.DerivedOrder;

@Service
public interface CryptoExchangeAsyncRestApiService {
  CompletableFuture<DerivedOrder[]> getOrderBook(
      String productId, String level, String userAction, boolean isReverseOrderBook);
}

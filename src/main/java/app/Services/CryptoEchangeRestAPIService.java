package app.Services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sun.media.jfxmedia.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import app.models.OrderBook;

@Service
public class CryptoEchangeRestAPIService {
  private static String PRODUCT_ID_KEY = "productId";
  private static String LEVEL_KEY = "level";

  @Autowired
  @Value("${app.Services.CryptoEchangeRestAPIService.GdaxRestHost}")
  private String GDAX_REST_HOST;
  private final RestTemplate mRestTemplate;

  public CryptoEchangeRestAPIService(RestTemplateBuilder builder) {
    mRestTemplate = builder.errorHandler(new ResponseErrorHandler() {
      @Override
      public boolean hasError(ClientHttpResponse response) throws IOException {
        Logger.logMsg(Logger.DEBUG, "Test");
        return response.getStatusCode() != HttpStatus.OK;
      }

      @Override
      public void handleError(ClientHttpResponse response) throws IOException {
        Logger.logMsg(Logger.DEBUG, "Test");
      }
    }).build();
  }

  public OrderBook getOrderBook(String productId, String level) {
    final String requestSubPath = "/products/{productId}/book?level={level}";
    final String fullRequestUrl = GDAX_REST_HOST + requestSubPath;
    Map<String, String> vars = new HashMap<>();
    vars.put(PRODUCT_ID_KEY, productId);
    vars.put(LEVEL_KEY, level);
    return mRestTemplate.getForObject(fullRequestUrl, OrderBook.class, vars);
  }
}

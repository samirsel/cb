package app.Validations;

public class GdaxOrderBookException extends RuntimeException {
  private final String messageKey;

  public GdaxOrderBookException(String messageKey) {
    super();
    this.messageKey = messageKey;
  }

  public String getMessageKey() {
    return messageKey;
  }
}

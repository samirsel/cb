package app.Validations;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import app.models.DefaultErrorInfo;
import app.models.ValidationError;

@ControllerAdvice
public class CryptoExchangeControllerAdvice extends ResponseEntityExceptionHandler {
  private static final String GENERAL_EXCEPTION_MESSAGE_KEY = "app.Validations.Exception.Message";

  @Autowired
  private MessageSource messageSource;

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    ValidationError error = ValidationError.fromBindingErrors(ex.getBindingResult());
    return super.handleExceptionInternal(ex, error, headers, status, request);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(value = GdaxOrderBookException.class)
  @ResponseBody
  public DefaultErrorInfo handleGdaxOrderBookException(
      GdaxOrderBookException exception, WebRequest request) {
    final String message = messageSource.getMessage(exception.getMessageKey(), null, "",
        Locale.ENGLISH);
    return new DefaultErrorInfo(message);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public DefaultErrorInfo handleDefaultException(Exception exception, WebRequest request)
      throws Exception {
    if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null) {
      throw exception;
    }
    final String message = messageSource.getMessage(GENERAL_EXCEPTION_MESSAGE_KEY, null, "",
        Locale.ENGLISH);
    return new DefaultErrorInfo(message);
  }
}
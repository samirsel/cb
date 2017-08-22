package app.Validations;

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
    return new DefaultErrorInfo(exception.getMessage());
  }

  //Todo:sselman: Fix this
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public DefaultErrorInfo handleDefaultException(
      Exception exception, WebRequest request) throws Exception {
    //Todo:sselman: Double check this.
    if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null) {
      throw exception;
    }

    return new DefaultErrorInfo("There was an internal server error. Please try again in a few " +
        "minutes.");
  }
}
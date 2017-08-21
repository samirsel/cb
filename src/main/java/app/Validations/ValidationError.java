package app.Validations;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class ValidationError {
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<String> errors = new ArrayList<>();

  private final String errorMessage;

  public static ValidationError fromBindingErrors(Errors errors) {
    ValidationError error = new ValidationError(
        "Validation failed. " + errors.getErrorCount() +" error(s)");
    for (ObjectError objectError : errors.getAllErrors()) {
      error.addValidationError(objectError.getDefaultMessage());
    }
    return error;
  }

  public void addValidationError(String error) {
    errors.add(error);
  }

  public List<String> getErrors() {
    return errors;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  private ValidationError(String errorMessage) {
    this.errorMessage = errorMessage;
  }

}

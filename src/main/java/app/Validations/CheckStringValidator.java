package app.Validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckStringValidator implements ConstraintValidator<CheckStringVal, String> {
  private String[] allowedVals;

  @Override
  public void initialize(CheckStringVal constraintAnnotation) {
    allowedVals = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return false;
    }
    for (String allowedVal : allowedVals) {
      if (allowedVal.equalsIgnoreCase(value)) {
        return true;
      }
    }
    return false;
  }
}

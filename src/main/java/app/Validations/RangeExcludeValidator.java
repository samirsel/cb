package app.Validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RangeExcludeValidator implements ConstraintValidator<RangeExclude, String> {
  private double[] excludedVals;

  @Override
  public void initialize(RangeExclude constraintAnnotation) {
    excludedVals = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    Double val;
    try {
      val = Double.valueOf(value);
    } catch (NumberFormatException ex) {
      return false;
    }

    for (double excludedVal : excludedVals) {
      if (excludedVal == val) {
        return false;
      }
    }
    return true;
  }
}

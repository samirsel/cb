package app.Validations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import org.hibernate.validator.constraints.Range;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = RangeExcludeValidator.class)
@Documented
@Range
@ReportAsSingleViolation
public @interface RangeExclude {

  String message() default "{app.Validations.RangeExclude.DefaultMessage}";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };

  double[] value();

  @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
  @Retention(RUNTIME)
  @Documented
  @interface List {
    RangeExclude[] value();
  }
}

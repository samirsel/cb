package app.Validations;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckStringValidator.class)
@Documented
public @interface CheckStringVal {

  String message() default "{app.Validations.CheckStringVal.DefaultMessage}";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };

  String[] value();

  @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
  @Retention(RUNTIME)
  @Documented
  @interface List {
    CheckStringVal[] value();
  }
}

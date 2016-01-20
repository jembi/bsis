package constraintvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LocationExistsConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LocationExists {

  String message() default "Invalid Location Type specified";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
package constraintvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PackTypeExistsConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PackTypeExists {

  String message() default "Invalid Pack Type specified";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
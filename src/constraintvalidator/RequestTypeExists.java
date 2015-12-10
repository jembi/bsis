package constraintvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RequestTypeExistsConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestTypeExists {

  String message() default "Invalid Request Type specified";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
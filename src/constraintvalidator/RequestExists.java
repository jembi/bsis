package constraintvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RequestExistsConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestExists {

  String message() default "Request does not exist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
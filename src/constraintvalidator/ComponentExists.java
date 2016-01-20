package constraintvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ComponentExistsConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentExists {

  String message() default "Component does not exist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
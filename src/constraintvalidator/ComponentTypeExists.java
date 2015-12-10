package constraintvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ComponentTypeExistsConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentTypeExists {

  String message() default "Invalid Component Type specified";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
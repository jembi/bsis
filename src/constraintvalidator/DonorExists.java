package constraintvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DonorExistsConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DonorExists {

  String message() default "Donor does not exist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
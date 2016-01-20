package constraintvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DonationExistsConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DonationExists {

  String message() default "Donation does not exist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
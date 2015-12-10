package constraintvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DonationBatchExistsConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DonationBatchExists {

  String message() default "Donation batch does not exist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
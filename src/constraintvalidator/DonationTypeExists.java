package constraintvalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DonationTypeExistsConstraintValidator.class)
public @interface DonationTypeExists {

  String message() default "Invalid Donation Type Specified";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
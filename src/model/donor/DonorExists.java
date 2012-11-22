package model.donor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = DonorExistsConstraintValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DonorExists {

  String message() default "Donor does not exist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
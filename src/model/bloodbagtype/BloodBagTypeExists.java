package model.bloodbagtype;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = BloodBagTypeExistsConstraintValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BloodBagTypeExists {

  String message() default "Invalid Blood Bag Type specified";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
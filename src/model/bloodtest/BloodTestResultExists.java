package model.bloodtest;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = BloodTestResultExistsConstraintValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BloodTestResultExists {

  String message() default "Blood Test Result does not exist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
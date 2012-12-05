package model.bloodtest;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = BloodTestExistsConstraintValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BloodTestExists {

  String message() default "Blood Test does not exist";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
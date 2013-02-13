package model.requesttype;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = RequestTypeExistsConstraintValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestTypeExists {

  String message() default "Invalid Request Type specified";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
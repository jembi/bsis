package org.jembi.bsis.constraintvalidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ComponentTypeExistsConstraintValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentTypeExists {

  String message() default "Invalid Component Type specified";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
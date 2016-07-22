package org.jembi.bsis.constraintvalidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = ComponentStatusIsConsistentConstraintValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentStatusIsConsistent {

  String message() default "Component status and inventory status are not consistent for this Component";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
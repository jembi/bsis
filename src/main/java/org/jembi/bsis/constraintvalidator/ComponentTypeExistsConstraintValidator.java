package org.jembi.bsis.constraintvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComponentTypeExistsConstraintValidator implements
    ConstraintValidator<ComponentTypeExists, ComponentType> {

  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  public ComponentTypeExistsConstraintValidator() {
  }

  @Override
  public void initialize(ComponentTypeExists constraint) {
  }

  public boolean isValid(ComponentType target, ConstraintValidatorContext context) {

    if (target == null)
      return true;

    try {
      if (componentTypeRepository.getComponentTypeById(target.getId()) != null)
        return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public void setComponentTypeRepository(ComponentTypeRepository componentTypeRepository) {
    this.componentTypeRepository = componentTypeRepository;
  }
}
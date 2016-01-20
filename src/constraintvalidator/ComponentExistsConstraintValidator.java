package constraintvalidator;

import model.component.Component;
import org.springframework.beans.factory.annotation.Autowired;
import repository.ComponentRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@org.springframework.stereotype.Component
class ComponentExistsConstraintValidator implements
        ConstraintValidator<ComponentExists, Component> {

  @Autowired
  private ComponentRepository componentRepository;

  public ComponentExistsConstraintValidator() {
  }

  @Override
  public void initialize(ComponentExists constraint) {
  }

  public boolean isValid(Component target, ConstraintValidatorContext context) {

    if (target == null)
      return true;

    try {

      Component component = null;

      if (target.getId() != null) {
        component = componentRepository.findComponentById(target.getId());
      }
      if (component != null) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public void setComponentRepository(ComponentRepository componentRepository) {
    this.componentRepository = componentRepository;
  }
}
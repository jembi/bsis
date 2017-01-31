package org.jembi.bsis.backingform.validator;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.repository.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ComponentBackingFormValidator extends BaseValidator<ComponentBackingForm> {
  @Autowired
  private ComponentRepository componentRepository;

  @Override
  public void validateForm(ComponentBackingForm form, Errors errors) {
    validateWeight(form, errors);
  }

  public void validateWeight(ComponentBackingForm form, Errors errors) {
    Integer weight = form.getWeight();
    if (weight != null && (weight <= 0 || weight >= 1000)) {
      errors.rejectValue("weight", "errors.invalid", "weight should be between 0 and 1000");
    }

    org.jembi.bsis.model.component.Component parentComponent = componentRepository.findComponent(form.getId()).getParentComponent();
    if (parentComponent != null && weight > parentComponent.getWeight()) {
       errors.rejectValue("weight", "errors.invalid", "weight should not be greater than parent component weight");
    }
  }

  @Override
  public String getFormName() {
    return "Component";
  }

}
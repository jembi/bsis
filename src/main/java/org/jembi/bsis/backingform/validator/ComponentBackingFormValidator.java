package org.jembi.bsis.backingform.validator;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ComponentBackingFormValidator extends BaseValidator<ComponentBackingForm> {
  
  @Override
  public void validateForm(ComponentBackingForm form, Errors errors) {
    Integer weight = form.getWeight();
    if (weight != null && (weight <= 0 || weight >= 1000)) {
      errors.rejectValue("weight", "weight.invalid", "weight should be between 0 and 1000");
    }
  }

  @Override
  public String getFormName() {
    return "Component";
  }

  @Override
  public boolean formHasBaseEntity() {
    return false;
  }
}

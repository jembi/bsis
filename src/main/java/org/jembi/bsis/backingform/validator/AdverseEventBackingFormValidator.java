package org.jembi.bsis.backingform.validator;

import org.jembi.bsis.backingform.AdverseEventBackingForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class AdverseEventBackingFormValidator extends BaseValidator<AdverseEventBackingForm> {

  @Override
  public void validateForm(AdverseEventBackingForm adverseEventBackingForm, Errors errors) {
    if (adverseEventBackingForm.getType() == null) {
      errors.rejectValue("type", "adverseEvent.type.required", "Adverse event type is required");
    }
  }

  @Override
  public String getFormName() {
    return "adverseEvent";
  }
}

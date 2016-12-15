package org.jembi.bsis.backingform.validator;

import java.util.Date;

import org.jembi.bsis.backingform.RecordComponentBackingForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class RecordComponentBackingFormValidator extends BaseValidator<RecordComponentBackingForm> {

  @Override
  public void validateForm(RecordComponentBackingForm form, Errors errors) {
    validateProcessedOnDate(form, errors);  

    if (form.getParentComponentId() == null) {
      errors.rejectValue("processedOn", "errors.required", "This is required");
    }
  }

  @Override
  public String getFormName() {
    return "RecordComponentBackingForm";
  }

  private void validateProcessedOnDate(RecordComponentBackingForm recordComponentBackingForm, Errors errors) {
    Date processedOn = recordComponentBackingForm.getProcessedOn();
    
    if (processedOn == null) {
      errors.rejectValue("processedOn","required.processedOn", "This is required");
    } else if (processedOn.before(new Date())) {
      errors.rejectValue("processedOn", "date.futureDate", "Cannot be a future date");
    }
  }
}

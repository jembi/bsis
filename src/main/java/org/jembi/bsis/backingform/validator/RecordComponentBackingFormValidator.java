package org.jembi.bsis.backingform.validator;

import java.util.Date;

import org.jembi.bsis.backingform.RecordComponentBackingForm;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.repository.ComponentTypeCombinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class RecordComponentBackingFormValidator extends BaseValidator<RecordComponentBackingForm> {

  @Autowired
  private ComponentTypeCombinationRepository componentTypeCombinationRepository;

  @Autowired
  private ComponentRepository componentRepository;

  @Override
  public void validateForm(RecordComponentBackingForm form, Errors errors) {

    // Validate processedOn
    if (form.getProcessedOn() == null) {
      errors.rejectValue("processedOn", "errors.required", "This is required");
    } else if (!form.getProcessedOn().before(new Date())) {
      errors.rejectValue("processedOn", "errors.invalid", "Cannot be a future date");
    } else if (form.getParentComponentId() != null) {
      Date createdOn = componentRepository.findComponentById(form.getParentComponentId()).getCreatedOn();
      if (form.getProcessedOn().before(createdOn)) {
        errors.rejectValue("processedOn", "errors.invalid", "Cannot be before parentComponent.createdOn");
      }
    }

    // Validate parentComponentId
    if (form.getParentComponentId() == null) {
      errors.rejectValue("parentComponentId", "errors.required", "This is required");
    } else if (!componentRepository.verifyComponentExists(form.getParentComponentId())) {
      errors.rejectValue("parentComponentId", "errors.invalid", "This is invalid");
    }

    // Validate componentTypeCombination
    if (form.getComponentTypeCombination() == null) {
      errors.rejectValue("componentTypeCombination", "errors.required", "This is required");
    } else if (form.getComponentTypeCombination().getId() == null) {
      errors.rejectValue("componentTypeCombination.id", "errors.required", "This is required");
    } else if (!componentTypeCombinationRepository.verifyComponentTypeCombinationExists(form.getComponentTypeCombination().getId())) {
      errors.rejectValue("componentTypeCombination.id", "errors.invalid", "This is invalid");
    }
  }

  @Override
  public String getFormName() {
    return "RecordComponentBackingForm";
  }

}

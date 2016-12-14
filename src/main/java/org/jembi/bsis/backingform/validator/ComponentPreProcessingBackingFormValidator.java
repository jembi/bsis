package org.jembi.bsis.backingform.validator;

import org.jembi.bsis.backingform.ComponentPreProcessingBackingForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Date;

@Component
public class ComponentPreProcessingBackingFormValidator extends BaseValidator<ComponentPreProcessingBackingForm> {

  @Override
  public void validateForm(ComponentPreProcessingBackingForm form, Errors errors) {
    validateWeight(form, errors);
    validateDonationBleedTimes(form, errors);
  }

  public void validateWeight(ComponentPreProcessingBackingForm form, Errors errors) {
    Integer weight = form.getWeight();
    if (weight != null && (weight <= 0 || weight >= 1000)) {
      errors.rejectValue("weight", "weight.invalid", "weight should be between 0 and 1000");
    }
  }

  private void validateDonationBleedTimes(ComponentPreProcessingBackingForm form, Errors errors) {
    Date bleedStartTime = form.getBleedStartTime();
    Date bleedEndTime = form.getBleedEndTime();
    long timeDiff = 0L;
    long timeDiffInMinutes = 0L;
    if (bleedEndTime !=null && bleedStartTime != null) {
      timeDiff = bleedEndTime.getTime() - bleedStartTime.getTime();// Difference in milliseconds
      timeDiffInMinutes = timeDiff / (60 * 1000) % 60;
    }

    if (bleedStartTime == null || bleedEndTime == null) {
      if (bleedStartTime == null) {
        errors.rejectValue("Component.donation.bleedStartTime", "bleedStartTime.empty", "This is required");
      }
      if (bleedEndTime == null) {
        errors.rejectValue("Component.donation.bleedEndTime", "bleedEndTime.empty", "This is required");
      }
    } else if (bleedStartTime != null && bleedEndTime != null && bleedStartTime.after(bleedEndTime)) {
      errors.rejectValue("Component.donation", "bleedEndTime.outOfRange", "Bleed End time should be after start time");
    } else if (timeDiffInMinutes <= 0) {
      errors.rejectValue("Component.donation", "bleedEndTime.invalid", "Bleed start time should not be equal to bleed end time");
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

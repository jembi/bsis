package org.jembi.bsis.backingform.validator;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.jembi.bsis.backingform.ComponentPreProcessingBackingForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

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
      errors.rejectValue("weight", "errors.invalid", "weight should be between 0 and 1000");
    }
  }

  private void validateDonationBleedTimes(ComponentPreProcessingBackingForm form, Errors errors) {
    Date bleedStartTime = form.getBleedStartTime();
    Date bleedEndTime = form.getBleedEndTime();
    long timeDiffInMinutes = 0L;
    if (bleedEndTime !=null && bleedStartTime != null) {
      //Get time Difference in minutes
      timeDiffInMinutes = TimeUnit.MILLISECONDS.toMinutes(bleedEndTime.getTime() - bleedStartTime.getTime());
    }

    if (bleedStartTime == null || bleedEndTime == null) {
      if (bleedStartTime == null) {
        errors.rejectValue("bleedStartTime", "errors.required", "This is required");
      }
      if (bleedEndTime == null) {
        errors.rejectValue("bleedEndTime", "errors.required", "This is required");
      }
    } else if (bleedStartTime.after(bleedEndTime)) {
      errors.rejectValue("bleedEndTime", "errors.invalid", "Bleed end time should be after start time");
    } else if (timeDiffInMinutes <= 0) {
      errors.rejectValue("bleedEndTime", "errors.invalid", "Start and end bleed times are the same: please re-enter");
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

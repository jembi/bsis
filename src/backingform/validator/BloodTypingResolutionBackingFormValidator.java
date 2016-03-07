package backingform.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import backingform.BloodTypingResolutionBackingForm;
import repository.bloodtesting.BloodTypingMatchStatus;

@Component
public class BloodTypingResolutionBackingFormValidator extends BaseValidator<BloodTypingResolutionBackingForm> {

  @Override
  public void validateForm(BloodTypingResolutionBackingForm form, Errors errors) {

    if (form.getStatus() == null) {
      errors.rejectValue("status", "bloodTypingResolution.status.required");
    } else if (!form.getStatus().equals(BloodTypingMatchStatus.RESOLVED)
        && !form.getStatus().equals(BloodTypingMatchStatus.NO_TYPE_DETERMINED)) {
      errors.rejectValue("status", "bloodTypingResolution.status.invalid", "Only valid statuses are: "
          + BloodTypingMatchStatus.RESOLVED + " and " + BloodTypingMatchStatus.NO_TYPE_DETERMINED);
    } else if (form.getStatus().equals(BloodTypingMatchStatus.RESOLVED)) {
      if (StringUtils.isEmpty(form.getBloodAbo())) {
        errors.rejectValue("bloodAbo", "bloodTypingResolution.bloodAbo.required");
      }
      if (StringUtils.isEmpty(form.getBloodRh())) {
        errors.rejectValue("bloodRh", "bloodTypingResolution.bloodRh.required");
      }
    }
  }

  @Override
  public String getFormName() {
    return "bloodTypingResolution";
  }

}

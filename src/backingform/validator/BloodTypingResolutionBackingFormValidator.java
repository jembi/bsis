package backingform.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import backingform.BloodTypingResolutionBackingForm;
import backingform.BloodTypingResolutionBackingForm.FinalBloodTypingMatchStatus;

@Component
public class BloodTypingResolutionBackingFormValidator extends BaseValidator<BloodTypingResolutionBackingForm> {

  @Override
  public void validateForm(BloodTypingResolutionBackingForm form, Errors errors) {

    if (form.getStatus().equals(FinalBloodTypingMatchStatus.RESOLVED)) {
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

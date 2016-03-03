package backingform.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import backingform.BloodTypingResolutionBackingForm;

@Component
public class BloodTypingResolutionBackingFormValidator extends BaseValidator<BloodTypingResolutionBackingForm> {

  @Override
  public void validateForm(BloodTypingResolutionBackingForm form, Errors errors) {

    if (form.getResolved() == null) {
      errors.rejectValue("resolved", "bloodTypingResolution.resolved.required");
    } else if (form.getResolved()) {
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

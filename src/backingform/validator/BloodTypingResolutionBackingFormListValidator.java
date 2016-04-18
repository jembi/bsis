package backingform.validator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import backingform.BloodTypingResolutionBackingForm;
import backingform.BloodTypingResolutionBackingFormList;
import repository.bloodtesting.BloodTypingMatchStatus;

@Component
public class BloodTypingResolutionBackingFormListValidator extends BaseValidator<BloodTypingResolutionBackingFormList> {

  @Override
  public void validateForm(BloodTypingResolutionBackingFormList bloodTypingResolutionBackingFormList, Errors errors) {
    List<BloodTypingResolutionBackingForm> forms = bloodTypingResolutionBackingFormList.getBloodTypingResolutions();
    for (int i = 0; i < forms.size(); i++) {
      validateForm(forms.get(i), errors, i);
    }
  }

  private void validateForm(BloodTypingResolutionBackingForm form, Errors errors, int index) {

    if (form.getStatus() == null) {
      errors.rejectValue("bloodTypingResolutions[" + index + "].status", "bloodTypingResolution.status.required");
    } else if (!form.getStatus().equals(BloodTypingMatchStatus.RESOLVED)
        && !form.getStatus().equals(BloodTypingMatchStatus.NO_TYPE_DETERMINED)) {
      errors.rejectValue("bloodTypingResolutions[" + index + "].status", "bloodTypingResolution.status.invalid",
          "Only valid statuses are: " + BloodTypingMatchStatus.RESOLVED + " and "
              + BloodTypingMatchStatus.NO_TYPE_DETERMINED);
    } else if (form.getStatus().equals(BloodTypingMatchStatus.RESOLVED)) {
      if (StringUtils.isEmpty(form.getBloodAbo())) {
        errors.rejectValue("bloodTypingResolutions[" + index + "].bloodAbo", "bloodTypingResolution.bloodAbo.required");
      }
      if (StringUtils.isEmpty(form.getBloodRh())) {
        errors.rejectValue("bloodTypingResolutions[" + index + "].bloodRh", "bloodTypingResolution.bloodRh.required");
      }
    }
  }

  @Override
  public String getFormName() {
    return "bloodTypingResolutionBackingFormList";
  }

}

package org.jembi.bsis.backingform.validator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.BloodTypingResolutionBackingForm;
import org.jembi.bsis.backingform.BloodTypingResolutionsBackingForm;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class BloodTypingResolutionsBackingFormValidator extends BaseValidator<BloodTypingResolutionsBackingForm> {

  @Override
  public void validateForm(BloodTypingResolutionsBackingForm backingForm, Errors errors) {
    List<BloodTypingResolutionBackingForm> forms = backingForm.getBloodTypingResolutions();
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
    return "bloodTypingResolutionsBackingForm";
  }

}

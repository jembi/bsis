package org.jembi.bsis.backingform.validator;

import org.jembi.bsis.backingform.PostDonationCounsellingBackingForm;
import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class PostDonationCounsellingBackingFormValidator extends BaseValidator<PostDonationCounsellingBackingForm> {

  @Override
  public void validateForm(PostDonationCounsellingBackingForm form, Errors errors) {
    
    if (form.getFlaggedForCounselling()) {
      if (form.getCounsellingDate() != null) {
        errors.rejectValue("counsellingDate", "errors.invalid", "Counselling Date should be empty");
      }
      if (form.getCounsellingStatus() != null) {
        errors.rejectValue("counsellingStatus", "errors.invalid", "Counselling Status should be empty");
      }
    } else {
      if (form.getCounsellingDate() == null) {
        errors.rejectValue("counsellingDate", "errors.required", "Counselling Date is required");
      }
      if (form.getCounsellingStatus() == null) {
        errors.rejectValue("counsellingStatus", "errors.required", "Counselling Status is required");
      } else {
        if (form.getCounsellingStatus().equals(CounsellingStatus.RECEIVED_COUNSELLING) && form.isReferred() == null) {
          errors.rejectValue("referred", "errors.invalid", "Referred is required");
        }
      }
    }
  }

  @Override
  public String getFormName() {
    return "postDonationCounselling";
  }
}
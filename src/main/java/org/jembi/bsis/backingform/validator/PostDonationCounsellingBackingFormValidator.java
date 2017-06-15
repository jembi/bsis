package org.jembi.bsis.backingform.validator;

import org.jembi.bsis.backingform.PostDonationCounsellingBackingForm;
import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.DateGeneratorService;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class PostDonationCounsellingBackingFormValidator extends BaseValidator<PostDonationCounsellingBackingForm> {

  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private DateGeneratorService dateGeneratorService;

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
      if (form.getCounsellingDate() != null) {
        LocalDate counsellingDate = dateGeneratorService.generateLocalDate(form.getCounsellingDate());
        LocalDate currentDate = dateGeneratorService.generateLocalDate();
        if (counsellingDate.isAfter(currentDate)) {
          errors.rejectValue("counsellingDate", "errors.invalid", "Counselling Date should not be in the future");
        }
      }
      if (form.getCounsellingStatus() == null) {
        errors.rejectValue("counsellingStatus", "errors.required", "Counselling Status is required");
      } else {
        if (form.getCounsellingStatus().equals(CounsellingStatus.RECEIVED_COUNSELLING) && form.isReferred() == null) {
          errors.rejectValue("referred", "errors.invalid", "Referred is required");
        }
      }
      if (form.isReferred() != null && form.isReferred()) {
        if (form.getReferralSite() == null) {
          errors.rejectValue("referralSite", "errors.required", "Referral site is required");
        }
        if (form.getReferralSite() != null) {
          Location aLocation = locationRepository.getLocation(form.getReferralSite().getId());
          if (!aLocation.getIsReferralSite()) {
            errors.rejectValue("referralSite", "errors.invalid", "Location must be a referral site");
          }
        }
      }
    }
  }

  @Override
  public String getFormName() {
    return "postDonationCounselling";
  }
}
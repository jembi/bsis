package org.jembi.bsis.backingform.validator;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.DonationTypeBackingForm;
import org.jembi.bsis.model.donationtype.DonationType;
import org.jembi.bsis.repository.DonationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class DonationTypeBackingFormValidator extends BaseValidator<DonationTypeBackingForm> {

  @Autowired
  private DonationTypeRepository donationTypeRepository;

  @Override
  public void validateForm(DonationTypeBackingForm form, Errors errors) {
    if (isDuplicateDonationType(form)) {
      errors.rejectValue("type", "400",
          "Donation type already exists.");
    }
  }

  @Override
  public String getFormName() {
    return "donationType";
  }

  private boolean isDuplicateDonationType(DonationTypeBackingForm form) {
    if (StringUtils.isBlank(form.getType())) {
      return false;
    }

    DonationType existingDonationType = donationTypeRepository.getDonationType(form.getType());
    if (existingDonationType != null && !existingDonationType.getId().equals(form.getId())) {
      return true;
    }

    return false;
  }
}
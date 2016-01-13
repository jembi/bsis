package backingform.validator;

import model.donationtype.DonationType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import repository.DonationTypeRepository;
import backingform.DonationTypeBackingForm;

@Component
public class DonationTypeBackingFormValidator extends BaseValidator<DonationTypeBackingForm> {
  
  @Autowired
  private DonationTypeRepository donationTypeRepository;

    @Override
    public void validateForm(DonationTypeBackingForm form, Errors errors) {
        if (isDuplicateDonationType(form.getDonationType())){
            errors.rejectValue("type", "400",
                    "Donation type already exists.");
        }
    }
    
  private boolean isDuplicateDonationType(DonationType donationType) {
    String type = donationType.getDonationType();
    if (StringUtils.isBlank(type)) {
      return false;
    }

    DonationType existingDonationType = donationTypeRepository.getDonationType(type);
    if (existingDonationType != null && !existingDonationType.getId().equals(donationType.getId())) {
      return true;
    }

    return false;
  }
}
package backingform.validator;

import javax.persistence.NoResultException;

import model.donation.Donation;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import repository.DonationRepository;
import utils.CustomDateFormatter;
import backingform.ComponentBackingForm;

@org.springframework.stereotype.Component
public class ComponentBackingFormValidator extends BaseValidator<ComponentBackingForm> {

  private static final Logger LOGGER = Logger.getLogger(ComponentBackingFormValidator.class);

  @Autowired
  DonationRepository donationRepository;

  @Override
  public void validateForm(ComponentBackingForm form, Errors errors) {
    String createdOn = form.getCreatedOn();
    if (!CustomDateFormatter.isDateTimeStringValid(createdOn))
      errors.rejectValue("component.createdOn", "dateFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());

    String expiresOn = form.getExpiresOn();
    if (!CustomDateFormatter.isDateStringValid(expiresOn))
      errors.rejectValue("component.expiresOn", "dateFormat.incorrect",
          CustomDateFormatter.getDateErrorMessage());

    Donation donation = findDonation(form.getDonationIdentificationNumber());
    form.setDonation(donation);
    if (donation == null) {
      errors.rejectValue("component.donationIdentificationNumber", "donation.invalid", "Please specify a valid donation");
    }

    commonFieldChecks(form, errors);
  }

  @Override
  public String getFormName() {
    return "component";
  }

  private Donation findDonation(String donationIdentificationNumber) {
    Donation donation = null;
    if (StringUtils.isNotBlank(donationIdentificationNumber)) {
      try {
        donation = donationRepository.findDonationByDonationIdentificationNumber(donationIdentificationNumber);
      } catch (NoResultException ex) {
        LOGGER.warn("Could not find donation with donationIdentificationNumber of '" + donationIdentificationNumber + "' . Error: " + ex.getMessage());
      }
    }
    return donation;
  }
}

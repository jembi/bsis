package backingform.validator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.NoResultException;

import model.donation.Donation;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import repository.DonationRepository;
import utils.CustomDateFormatter;
import backingform.ComponentCombinationBackingForm;

import com.fasterxml.jackson.databind.ObjectMapper;

@org.springframework.stereotype.Component
public class ComponentCombinationBackingFormValidator extends BaseValidator<ComponentCombinationBackingForm> {
  
  private static final Logger LOGGER = Logger.getLogger(ComponentCombinationBackingFormValidator.class);
  
  @Autowired
  DonationRepository donationRepository;

  @Override
  public void validateForm(ComponentCombinationBackingForm form, Errors errors) {

    if (StringUtils.isBlank(form.getComponentTypeCombination()))
      errors.rejectValue("componentTypeCombination", "component.componentTypeCombination",
          "Component type combination should be specified");

    String createdOn = form.getCreatedOn();
    if (!CustomDateFormatter.isDateTimeStringValid(createdOn))
      errors.rejectValue("component.createdOn", "dateFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());

    String expiresOn = form.getExpiresOn();
    ObjectMapper mapper = new ObjectMapper();

    try {
      @SuppressWarnings("unchecked")
      Map<String, String> expiryDateByComponentType = mapper.readValue(expiresOn, HashMap.class);
      for (String componentTypeId : expiryDateByComponentType.keySet()) {
        String expiryDate = expiryDateByComponentType.get(componentTypeId);
        if (!CustomDateFormatter.isDateTimeStringValid(expiryDate))
          errors.rejectValue("component.expiresOn", "dateFormat.incorrect",
              CustomDateFormatter.getDateErrorMessage());
      }
    } catch (IOException e) {
      LOGGER.error("Exception while checking component.expiresOn '" + expiresOn + "'. Error: "+e.getMessage(), e);
    }

    updateRelatedEntities(form);

    commonFieldChecks(form, errors);
  }
  
  @Override
  public String getFormName() {
    return "component";
  }
  
  private void updateRelatedEntities(ComponentCombinationBackingForm form) {
    Donation donation = null;
    String donationIdentificationNumber = form.getDonationIdentificationNumber();
    if (StringUtils.isNotBlank(donationIdentificationNumber)) {
      try {
        donation = donationRepository.findDonationByDonationIdentificationNumber(donationIdentificationNumber);
      } catch (NoResultException ex) {
        LOGGER.warn("Could not find donation with donationIdentificationNumber of '" + donationIdentificationNumber + "' . Error: " + ex.getMessage());
      }
    }
    form.setDonation(donation);
  }
}

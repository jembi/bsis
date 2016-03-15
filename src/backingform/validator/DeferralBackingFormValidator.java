package backingform.validator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.persistence.NoResultException;

import backingform.DeferralBackingForm;
import model.donor.Donor;
import model.location.Location;
import repository.DonorRepository;
import repository.LocationRepository;

@Component
public class DeferralBackingFormValidator extends BaseValidator<DeferralBackingForm> {

  private static final Logger LOGGER = Logger.getLogger(DeferralBackingFormValidator.class);

  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private DonorRepository donorRepository;

  @Override
  public void validateForm(DeferralBackingForm form, Errors errors) {
    
    if (form.getDeferralReason() == null) {
      errors.rejectValue("deferralReason", "deferral.deferralReason.required", "Deferral reason is required");
    }
    
    if (form.getVenue() == null) {
      errors.rejectValue("venue", "deferral.venue.required", "Venue is required");
    } else {
      Location location = null;
      try {
        location = locationRepository.getLocation(form.getVenue().getId());
      } catch (NoResultException ex) {
        LOGGER.warn("Location not found id: " + form.getVenue().getId());
      }
      if (location == null) {
        errors.rejectValue("venue", "deferral.venue.required", "Venue does not exist");
      }
    }
    
    if (form.getDeferredUntil() == null) {
      errors.rejectValue("deferredUntil", "deferral.deferredUntil.required", "Deferred until is required");
    }
    
    if (form.getDeferredDonor() == null) {
      errors.rejectValue("deferredDonor", "deferral.deferredDonor.required", "Deferred donor is required");
    } else {
      Donor deferredDonor = null;
      try {
        deferredDonor = donorRepository.findDonorById(form.getDeferredDonor());
      } catch (NoResultException nre) {
        LOGGER.warn("Donor not found for id: " + form.getDeferredDonor());
      }
      if (deferredDonor == null) {
        errors.rejectValue("deferredDonor", "deferral.deferredDonor.required", "Deferred donor does not exist");
      }
    }
  }

  @Override
  public String getFormName() {
    return "deferral";
  }
}

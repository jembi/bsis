package backingform.validator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.persistence.NoResultException;

import backingform.DeferralBackingForm;
import model.location.Location;
import repository.LocationRepository;

@Component
public class DeferralBackingFormValidator extends BaseValidator<DeferralBackingForm> {

  private static final Logger LOGGER = Logger.getLogger(DeferralBackingFormValidator.class);

  @Autowired
  private LocationRepository locationRepository;

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
  }

  @Override
  public String getFormName() {
    return "deferral";
  }
}

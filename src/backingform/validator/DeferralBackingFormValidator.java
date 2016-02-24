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
  @Autowired
  LocationRepository locationRepository;
  private static final Logger LOGGER = Logger.getLogger(DeferralBackingFormValidator.class);

  @Override
  public void validateForm(DeferralBackingForm form, Errors errors) {
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
  }

  @Override
  public String getFormName() {
    return "deferral";
  }
}

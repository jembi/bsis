package backingform.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import backingform.DeferralBackingForm;
import repository.LocationRepository;

@Component
public class DeferralBackingFormValidator extends BaseValidator<DeferralBackingForm> {
  @Autowired
  LocationRepository locationRepository;

  @Override
  public void validateForm(DeferralBackingForm form, Errors errors) {
    if (form.getVenue() == null) {
      errors.rejectValue("venue", "deferral.venue.required", "Venue is required");
    } else {
      if (locationRepository.findById(form.getVenue().getId()) == null) {
        errors.rejectValue("venue", "deferral.venue.required", "Venue does not exist");
      }
    }
  }

  @Override
  public String getFormName() {
    return "deferral";
  }
}

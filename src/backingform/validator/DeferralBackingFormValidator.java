package backingform.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import backingform.DeferralBackingForm;

@Component
public class DeferralBackingFormValidator extends BaseValidator<DeferralBackingForm> {

  @Override
  public void validateForm(DeferralBackingForm form, Errors errors) {
    if (form.getVenue() == null) {
      errors.rejectValue("venue", "deferral.venue.required", "Venue is required");
    }
  }

  @Override
  public String getFormName() {
    return "deferral";
  }
}

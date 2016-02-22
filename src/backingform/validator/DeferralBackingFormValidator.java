package backingform.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import backingform.DeferralBackingForm;

@Component
public class DeferralBackingFormValidator extends BaseValidator<DeferralBackingForm> {

  @Override
  public void validateForm(DeferralBackingForm form, Errors errors) {
    if (form.getVenueId() == null) {
      errors.rejectValue("venue", "deferral.venueId.required", "Deferral venueId is required");
    }
  }

  @Override
  public String getFormName() {
    return "deferral";
  }
}

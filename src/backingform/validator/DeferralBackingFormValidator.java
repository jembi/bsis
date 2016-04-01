package backingform.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import repository.DonorRepository;
import repository.LocationRepository;
import backingform.DeferralBackingForm;

@Component
public class DeferralBackingFormValidator extends BaseValidator<DeferralBackingForm> {

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
      if (!locationRepository.verifyLocationExists(form.getVenue().getId())) {
        errors.rejectValue("venue", "deferral.venue.required", "Venue does not exist");
      }
    }
    
    if (form.getDeferredUntil() == null) {
      errors.rejectValue("deferredUntil", "deferral.deferredUntil.required", "Deferred until is required");
    }
    
    if (form.getDeferredDonor() == null) {
      errors.rejectValue("deferredDonor", "deferral.deferredDonor.required", "Deferred donor is required");
    } else {
      if (!donorRepository.verifyDonorExists(form.getDeferredDonor())) {
        errors.rejectValue("deferredDonor", "deferral.deferredDonor.required", "Deferred donor does not exist");
      }
    }
  }

  @Override
  public String getFormName() {
    return "deferral";
  }
}

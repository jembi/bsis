package backingform.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import backingform.DeferralBackingForm;
import repository.DeferralReasonRepository;
import repository.DonorRepository;
import repository.LocationRepository;

@Component
public class DeferralBackingFormValidator extends BaseValidator<DeferralBackingForm> {

  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private DonorRepository donorRepository;
  @Autowired
  private DeferralReasonRepository deferralReasonRepository;

  @Override
  public void validateForm(DeferralBackingForm form, Errors errors) {

    commonFieldChecks(form, errors);

    if (form.getVenue() != null && !locationRepository.verifyLocationExists(form.getVenue().getId())) {
      errors.rejectValue("venue", "deferral.venue.required", "Venue does not exist");
    }
  
    if (form.getDeferredDonor() != null && !donorRepository.verifyDonorExists(form.getDeferredDonor().getId())) {
      errors.rejectValue("deferredDonor", "deferral.deferredDonor.required", "Deferred donor does not exist");
    }
    
    if (form.getDeferralReason() != null && !deferralReasonRepository.verifyDeferralReasonExists(form.getDeferralReason().getId())) {
      errors.rejectValue("deferralReason", "deferral.deferralReason.required", "Deferral reason does not exist");
    }
  }

  @Override
  public String getFormName() {
    return "DonorDeferral";
  }

  @Override
  protected boolean formHasBaseEntity() {
    return false;
  }
}

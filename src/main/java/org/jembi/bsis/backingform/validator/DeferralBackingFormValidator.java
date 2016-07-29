package org.jembi.bsis.backingform.validator;


import org.jembi.bsis.backingform.DeferralBackingForm;
import org.jembi.bsis.repository.DeferralReasonRepository;
import org.jembi.bsis.repository.DonorRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

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
    
    if (form.getDeferralDate() != null && form.getDeferredUntil() != null  && form.getDeferredUntil().before(form.getDeferralDate())) {
      errors.rejectValue("deferredUntil", "deferral.deferredUntil.invalid","Deferral end date can't be ealier than deferral date");
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

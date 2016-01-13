package backingform.validator;

import model.donordeferral.DeferralReason;
import model.donordeferral.DurationType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import repository.DeferralReasonRepository;
import backingform.DeferralReasonBackingForm;

@Component
public class DeferralReasonBackingFormValidator extends BaseValidator<DeferralReasonBackingForm> {
  
  @Autowired
  private DeferralReasonRepository deferralReasonRepository;

    @Override
    public void validateForm(DeferralReasonBackingForm form, Errors errors) {
        if (isDuplicateDeferralReason(form.getDeferralReason())) {
            errors.rejectValue("reason", "400", "Deferral Reason already exists.");
        }
        
        if (form.getDurationType() != DurationType.PERMANENT && (form.getDefaultDuration() == null ||
                form.getDefaultDuration() <= 0)) {
            errors.rejectValue("defaultDuration", "400", "Default duration must be a positive number of days");
        }
    }
    

  private boolean isDuplicateDeferralReason(DeferralReason deferralReason) {
    String reason = deferralReason.getReason();
    if (StringUtils.isBlank(reason)) {
      return false;
    }

    DeferralReason existingDeferralReason = deferralReasonRepository.findDeferralReason(reason);
    if (existingDeferralReason != null && !existingDeferralReason.getId().equals(deferralReason.getId())) {
      return true;
    }

    return false;
  }
}
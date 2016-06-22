package org.jembi.bsis.backingform.validator;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.DeferralReasonBackingForm;
import org.jembi.bsis.model.donordeferral.DeferralReason;
import org.jembi.bsis.model.donordeferral.DurationType;
import org.jembi.bsis.repository.DeferralReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

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

  @Override
  public String getFormName() {
    return "deferralReason";
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
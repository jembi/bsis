package org.jembi.bsis.backingform.validator;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.DiscardReasonBackingForm;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.repository.DiscardReasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class DiscardReasonBackingFormValidator extends BaseValidator<DiscardReasonBackingForm> {

  @Autowired
  private DiscardReasonRepository discardReasonRepository;

  @Override
  public void validateForm(DiscardReasonBackingForm form, Errors errors) {

    if (isDuplicateDiscardReason(form.getId(), form.getReason())) {
      errors.rejectValue("reason", "400",
          "Discard Reason already exists.");
    }
  }

  @Override
  public String getFormName() {
    return "discardReason";
  }

  private boolean isDuplicateDiscardReason(UUID id, String reason) {
    if (StringUtils.isBlank(reason)) {
      return false;
    }

    ComponentStatusChangeReason existingDiscardReason = discardReasonRepository.findDiscardReason(reason);
    if (existingDiscardReason != null && !existingDiscardReason.getId().equals(id)) {
      return true;
    }

    return false;
  }
}
package backingform.validator;

import model.componentmovement.ComponentStatusChangeReason;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import repository.DiscardReasonRepository;
import backingform.DiscardReasonBackingForm;

@Component
public class DiscardReasonBackingFormValidator extends BaseValidator<DiscardReasonBackingForm> {

    private DiscardReasonRepository discardReasonRepository;

    @Override
    public void validateForm(DiscardReasonBackingForm form, Errors errors) {

        if (isDuplicateDiscardReason(form.getDiscardReason())){
            errors.rejectValue("reason", "400",
                    "Discard Reason already exists.");
        }
    }
    
  private boolean isDuplicateDiscardReason(ComponentStatusChangeReason discardReason) {
    String reason = discardReason.getStatusChangeReason();
    if (StringUtils.isBlank(reason)) {
      return false;
    }

    ComponentStatusChangeReason existingDiscardReason = discardReasonRepository.findDiscardReason(reason);
    if (existingDiscardReason != null && !existingDiscardReason.getId().equals(discardReason.getId())) {
      return true;
    }

    return false;
  }
}
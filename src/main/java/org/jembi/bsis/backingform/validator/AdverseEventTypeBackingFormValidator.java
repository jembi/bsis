package org.jembi.bsis.backingform.validator;

import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.AdverseEventTypeBackingForm;
import org.jembi.bsis.repository.AdverseEventTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class AdverseEventTypeBackingFormValidator extends BaseValidator<AdverseEventTypeBackingForm> {

  @Autowired
  private AdverseEventTypeRepository adverseEventTypeRepository;

  @Override
  public void validateForm(AdverseEventTypeBackingForm adverseEventTypeBackingForm, Errors errors) {
    List<UUID> existingAdverseEventTypeIds = adverseEventTypeRepository.findIdsByName(adverseEventTypeBackingForm.getName());
    for (UUID id : existingAdverseEventTypeIds) {
      if (!id.equals(adverseEventTypeBackingForm.getId())) {
        errors.rejectValue("name", "adverseEventType.name.duplicate",
            "There is already an adverse event type with that name");
        break;
      }
    }
  }

  @Override
  public String getFormName() {
    return "adverseEventType";
  }
}

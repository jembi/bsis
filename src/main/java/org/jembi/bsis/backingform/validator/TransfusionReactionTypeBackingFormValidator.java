package org.jembi.bsis.backingform.validator;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.TransfusionReactionTypeBackingForm;
import org.jembi.bsis.repository.TransfusionReactionTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class TransfusionReactionTypeBackingFormValidator extends BaseValidator<TransfusionReactionTypeBackingForm> {
  
  @Autowired
  private TransfusionReactionTypeRepository transfusionReactionTypeRepository; 

  @Override
  public void validateForm(TransfusionReactionTypeBackingForm form, Errors errors) {
    
    // Validate Transfusion reaction type name
    if (StringUtils.isBlank(form.getName())) {
      errors.rejectValue("reactionName", "errors.required", "Reaction name is required");
    } else {
      if (!transfusionReactionTypeRepository.isUniqueTransfusionReactionTypeName(form.getId(), form.getName())) {
        errors.rejectValue("name", "errors.unique", "Reaction name already exists");
      }
    }
    
    if (form.getIsDeleted() == null) {
      errors.rejectValue("isDeleted", "errors.required", "Enabled state is required");
    }
  }

  @Override
  public String getFormName() {
    return "TransfusionReactionType";
  }

}

package org.jembi.bsis.backingform.validator;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.ComponentTypeCombinationBackingForm;
import org.jembi.bsis.repository.ComponentTypeCombinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ComponentTypeCombinationBackingFormValidator extends BaseValidator<ComponentTypeCombinationBackingForm> {

  private static final Integer MAX_LENGTH_NAME = 50;

  @Autowired
  ComponentTypeCombinationRepository componentTypeCombinationRepository;

  @Override
  public void validateForm(ComponentTypeCombinationBackingForm form, Errors errors) {
    if (StringUtils.isBlank(form.getCombinationName())) {
      errors.rejectValue("componentTypeName", "errors.required", "Component type name is required");
    } else if (form.getCombinationName().length() > MAX_LENGTH_NAME) {
      errors.rejectValue("componentTypeName", "fieldLength.error", "Maximum length for this field is " + MAX_LENGTH_NAME);
    }
  }

  @Override
  public String getFormName() {
        return "ComponentTypeCombination";
    }
}

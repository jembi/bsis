package org.jembi.bsis.backingform.validator;

import org.jembi.bsis.backingform.ComponentUsageBackingForm;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.repository.ComponentRepository;
import org.jembi.bsis.utils.CustomDateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

@org.springframework.stereotype.Component
public class UsageBackingFormValidator extends BaseValidator<ComponentUsageBackingForm> {

  @Autowired
  private ComponentRepository componentRepository;

  @Override
  public void validateForm(ComponentUsageBackingForm form, Errors errors) {
    String usageDate = form.getUsageDate();
    if (!CustomDateFormatter.isDateTimeStringValid(usageDate))
      errors.rejectValue("usage.usageDate", "dateFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());

    updateRelatedEntities(form);

    commonFieldChecks(form, errors);
  }

  @Override
  public String getFormName() {
    return "usage";
  }

  private void updateRelatedEntities(ComponentUsageBackingForm form) {
    Component component = componentRepository.findComponentById(Long.parseLong(form.getComponentId()));
    form.setComponent(component);
  }
}
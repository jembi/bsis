package backingform.validator;

import java.util.Arrays;

import model.component.Component;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import utils.CustomDateFormatter;
import viewmodel.ComponentUsageViewModel;
import viewmodel.RequestViewModel;
import backingform.ComponentUsageBackingForm;
import controller.UtilController;

public class UsageBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public UsageBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(ComponentUsageBackingForm.class, ComponentUsageViewModel.class, RequestViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    ComponentUsageBackingForm form = (ComponentUsageBackingForm) obj;

    String usageDate = form.getUsageDate();
    if (!CustomDateFormatter.isDateTimeStringValid(usageDate))
      errors.rejectValue("usage.usageDate", "dateFormat.incorrect",
          CustomDateFormatter.getDateTimeErrorMessage());

    updateRelatedEntities(form);

    utilController.commonFieldChecks(form, "usage", errors);
  }

  private void updateRelatedEntities(ComponentUsageBackingForm form) {
    Component component = utilController.findComponentById(form.getComponentId());
    form.setComponent(component);
  }
}


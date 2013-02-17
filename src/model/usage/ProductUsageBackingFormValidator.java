package model.usage;

import java.util.Arrays;

import model.CustomDateFormatter;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import controller.UtilController;

import viewmodel.UsageViewModel;

public class ProductUsageBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public ProductUsageBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(ProductUsageBackingForm.class, UsageViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    ProductUsageBackingForm form = (ProductUsageBackingForm) obj;

    String usageDate = form.getUsageDate();
    if (!CustomDateFormatter.isDateTimeStringValid(usageDate))
      errors.rejectValue("usage.usageDate", "dateFormat.incorrect",
          CustomDateFormatter.getErrorMessage());
    utilController.commonFieldChecks(form, "usage", errors);
  }
}

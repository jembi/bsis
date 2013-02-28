package model.compatibility;

import java.util.Arrays;

import model.CustomDateFormatter;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import viewmodel.CompatibilityTestViewModel;
import controller.UtilController;

public class CompatibilityTestBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public CompatibilityTestBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(CompatibilityTestBackingForm.class, CompatibilityTest.class, CompatibilityTestViewModel.class, CompatibilityTestBackingForm.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    CompatibilityTestBackingForm form = (CompatibilityTestBackingForm) obj;
    String crossmatchTestDate = form.getCompatibilityTestDate();
    if (!CustomDateFormatter.isDateTimeStringValid(crossmatchTestDate)) {
      errors.rejectValue("compatiblityTest.compatibilityTestDate", "dateFormat.incorrect",
          CustomDateFormatter.getErrorMessage());
    }
    utilController.commonFieldChecks(form, "CompatibilityTest", errors);
  }
}

package model.crossmatch;

import java.util.Arrays;

import model.CustomDateFormatter;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import viewmodel.CrossmatchTestViewModel;
import controller.UtilController;

public class CrossmatchTestBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public CrossmatchTestBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(CrossmatchTestBackingForm.class, CrossmatchTest.class, CrossmatchTestViewModel.class, CrossmatchTestBackingForm.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    CrossmatchTestBackingForm form = (CrossmatchTestBackingForm) obj;
    String crossmatchTestDate = form.getCrossmatchTestDate();
    if (!CustomDateFormatter.isDateTimeStringValid(crossmatchTestDate)) {
      errors.rejectValue("crossmatchtest.crossmatchTestDate", "dateFormat.incorrect",
          CustomDateFormatter.getErrorMessage());
    }
    utilController.commonFieldChecks(form, "crossmatchTest", errors);
  }
}

package model.collectedsample;

import java.util.Arrays;

import model.CustomDateFormatter;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import viewmodel.CollectedSampleViewModel;

import controller.UtilController;

public class CollectedSampleBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public CollectedSampleBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(FindCollectedSampleBackingForm.class, CollectedSampleBackingForm.class, CollectedSample.class, CollectedSampleViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    System.out.println("validating");
    System.out.println(validator);
    System.out.println(obj);
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    CollectedSampleBackingForm form = (CollectedSampleBackingForm) obj;
    String collectedOn = form.getCollectedOn();
    if (!CustomDateFormatter.isDateStringValid(collectedOn))
      errors.rejectValue("collectedSample.collectedOn", "dateFormat.incorrect",
          CustomDateFormatter.getErrorMessage());
    utilController.commonFieldChecks(form, "collectedSample", errors);
  }
}

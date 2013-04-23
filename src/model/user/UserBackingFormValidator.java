package model.user;

import java.util.Arrays;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import viewmodel.UserViewModel;
import controller.UtilController;

public class UserBackingFormValidator implements Validator {

  private Validator validator;
  @SuppressWarnings("unused")
  private UtilController utilController;

  public UserBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(UserBackingForm.class, User.class, UserViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
  }
}

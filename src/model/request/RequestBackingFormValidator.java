package model.request;

import java.util.Arrays;

import model.CustomDateFormatter;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import controller.UtilController;

import viewmodel.RequestViewModel;

public class RequestBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public RequestBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(RequestBackingForm.class, Request.class, RequestViewModel.class, FindRequestBackingForm.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    RequestBackingForm form = (RequestBackingForm) obj;
    String requestDate = form.getRequestDate();
    if (!CustomDateFormatter.isDateTimeStringValid(requestDate)) {
      errors.rejectValue("request.requestDate", "dateFormat.incorrect",
          CustomDateFormatter.getErrorMessage());
    }
    String requiredDate = form.getRequiredDate();
    if (!CustomDateFormatter.isDateStringValid(requiredDate)) {
      errors.rejectValue("request.requiredDate", "dateFormat.incorrect",
            CustomDateFormatter.getErrorMessage());
    }

    utilController.commonFieldChecks(form, "request", errors);
  }
}

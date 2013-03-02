package model.donor;

import java.util.Arrays;

import model.CustomDateFormatter;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import viewmodel.DonorViewModel;
import controller.UtilController;

public class DonorBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public DonorBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(DonorBackingForm.class, Donor.class, DonorViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
    DonorBackingForm form = (DonorBackingForm) obj;
    String birthDate = form.getBirthDate();
    if (!CustomDateFormatter.isDateStringValid(birthDate)) {
      errors.rejectValue("donor.birthDate", "dateFormat.incorrect",
          CustomDateFormatter.getDateErrorMessage());
    }
    utilController.commonFieldChecks(form, "donor", errors);
  }
}

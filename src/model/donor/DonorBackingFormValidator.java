package model.donor;

import java.util.Arrays;
import java.util.Map;

import model.CustomDateFormatter;

import org.apache.commons.lang3.StringUtils;
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
    validateDonorAge(form, errors);
    validateDonorHistory(form, errors);
    utilController.commonFieldChecks(form, "donor", errors);
  }

  private void validateDonorAge(DonorBackingForm form, Errors errors) {

    String birthDate = form.getBirthDate();

    if (birthDate != null)
    if (!CustomDateFormatter.isDateStringValid(birthDate)) {
      errors.rejectValue("donor.birthDate", "dateFormat.incorrect",
          CustomDateFormatter.getDateErrorMessage());
    }

    String age = form.getAge();
    if (age.equals("INVALID"))
      errors.rejectValue("donor.age", "ageFormat.incorrect", "Age should be number of years.");

//    boolean birthDateSpecified = StringUtils.isNotBlank(birthDate);
//    boolean ageSpecified = StringUtils.isNotBlank(age);
//    if (birthDateSpecified && ageSpecified) {
//      errors.rejectValue("donor.birthDate", "birthdate.ambiguous", "Both birth date and age specified.");
//      errors.rejectValue("donor.age", "age.ambiguous", "Both birth date and age specified.");
//    }

    Map<String, String> config = utilController.getConfigProperty("donationRequirements");
    if (config.get("ageLimitsEnabled").equals("true")) {
      try {
        Integer minAge = Integer.parseInt(config.get("minimumAge"));
        Integer maxAge = Integer.parseInt(config.get("maximumAge"));
        Integer donorAge = DonorUtils.computeDonorAge(form.getDonor());
        if (donorAge == null) {
          errors.rejectValue("donor.age", "donorage.missing", "One of donor Date of Birth or Age must be specified");
        }
        else {
          if (donorAge < minAge && donorAge > maxAge) {
            errors.rejectValue("donor.age", "donorage.outofrange",
              "Donor age is " + donorAge + " years." +
              "Donor age must be between " + minAge + " and " + maxAge + " years.");
          }
        }
      } catch (NumberFormatException ex) {
        ex.printStackTrace();
      }
    }
  }

  private void validateDonorHistory(DonorBackingForm form, Errors errors) {
  }

}

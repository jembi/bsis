package backingform.validator;

import backingform.DonationTypeBackingForm;
import controller.UtilController;
import model.donationtype.DonationType;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import repository.DonationTypeRepository;
import viewmodel.DonationTypeViewModel;

import java.util.Arrays;

public class DonationTypeBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;
  private DonationTypeRepository donationTypeRepository;

  public DonationTypeBackingFormValidator(Validator validator, UtilController utilController, DonationTypeRepository donationTypeRepository) {
    super();
    this.validator = validator;
    this.utilController = utilController;
    this.donationTypeRepository = donationTypeRepository;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(DonationTypeBackingForm.class, DonationType.class, DonationTypeViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {

    if (obj == null || validator == null)
      return;

    ValidationUtils.invokeValidator(validator, obj, errors);
    DonationTypeBackingForm form = (DonationTypeBackingForm) obj;

    if (utilController.isDuplicateDonationType(form.getDonationType())) {
      errors.rejectValue("type", "400",
          "Donation type already exists.");
    }
  }
}
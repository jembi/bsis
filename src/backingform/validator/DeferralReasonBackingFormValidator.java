package backingform.validator;

import backingform.DeferralReasonBackingForm;
import controller.UtilController;
import model.donordeferral.DeferralReason;
import model.donordeferral.DurationType;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import viewmodel.DeferralReasonViewModel;

import java.util.Arrays;

public class DeferralReasonBackingFormValidator implements Validator {

  private UtilController utilController;

  public DeferralReasonBackingFormValidator(UtilController utilController) {
    super();
    this.utilController = utilController;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(DeferralReasonBackingForm.class, DeferralReason.class, DeferralReasonViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {

    if (obj == null) {
      return;
    }

    DeferralReasonBackingForm form = (DeferralReasonBackingForm) obj;

    if (utilController.isDuplicateDeferralReason(form.getDeferralReason())) {
      errors.rejectValue("reason", "400", "Deferral Reason already exists.");
    }

    if (form.getDurationType() != DurationType.PERMANENT && (form.getDefaultDuration() == null ||
        form.getDefaultDuration() <= 0)) {
      errors.rejectValue("defaultDuration", "400", "Default duration must be a positive number of days");
    }
  }
}
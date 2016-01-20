package backingform.validator;

import backingform.DiscardReasonBackingForm;
import controller.UtilController;
import model.componentmovement.ComponentStatusChangeReason;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import repository.DiscardReasonRepository;
import viewmodel.DiscardReasonViewModel;

import java.util.Arrays;

public class DiscardReasonBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;
  private DiscardReasonRepository deferralReasonRepository;

  public DiscardReasonBackingFormValidator(Validator validator, UtilController utilController, DiscardReasonRepository deferralReasonRepository) {
    super();
    this.validator = validator;
    this.utilController = utilController;
    this.deferralReasonRepository = deferralReasonRepository;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(DiscardReasonBackingForm.class, ComponentStatusChangeReason.class, DiscardReasonViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {

    if (obj == null || validator == null)
      return;

    ValidationUtils.invokeValidator(validator, obj, errors);
    DiscardReasonBackingForm form = (DiscardReasonBackingForm) obj;

    if (utilController.isDuplicateDiscardReason(form.getDiscardReason())) {
      errors.rejectValue("reason", "400",
              "Discard Reason already exists.");
    }


  }
}
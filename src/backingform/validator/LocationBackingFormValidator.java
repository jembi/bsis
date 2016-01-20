package backingform.validator;

import backingform.LocationBackingForm;
import controller.UtilController;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Collections;

public class LocationBackingFormValidator implements Validator {

  private Validator validator;

  private UtilController utilController;

  public LocationBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return Collections.singletonList(LocationBackingForm.class).contains(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    if (target == null || validator == null) {
      return;
    }

    ValidationUtils.invokeValidator(validator, target, errors);

    LocationBackingForm form = (LocationBackingForm) target;

    if (utilController.isDuplicateLocationName(form.getLocation()))
      errors.rejectValue("name", "400", "Location name already exists.");
  }


}

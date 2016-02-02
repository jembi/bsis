package backingform.validator;

import backingform.PackTypeBackingForm;
import controller.UtilController;
import model.packtype.PackType;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import viewmodel.PackTypeViewModel;

import java.util.Arrays;

public class PackTypeBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;

  public PackTypeBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(PackTypeBackingForm.class, PackType.class, PackTypeViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {

    if (obj == null || validator == null)
      return;

    ValidationUtils.invokeValidator(validator, obj, errors);
    PackTypeBackingForm form = (PackTypeBackingForm) obj;

    if (!form.getType().getTestSampleProduced() && form.getType().getCountAsDonation()) {
      errors.rejectValue("type.countAsDonation", "countAsDonation.notAllowed", "Pack types that don't produce " +
          "a test sample cannot be counted as a donation");
    }

    if (utilController.isDuplicatePackTypeName(form.getType())) {
      errors.rejectValue("type.packType", "name.exists", "Pack Type name already exists.");
    }


  }
}

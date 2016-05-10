package backingform.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import backingform.BloodTransportBoxBackingForm;

@Component
public class BloodTransportBoxBackingFormValidator extends BaseValidator<BloodTransportBoxBackingForm> {
  
  @Override
  public void validateForm(BloodTransportBoxBackingForm form, Errors errors) {
    // common validations (length, required)
    commonFieldChecks(form, errors);
  }

  @Override
  public String getFormName() {
    return "BloodTransportBox";
  }
}

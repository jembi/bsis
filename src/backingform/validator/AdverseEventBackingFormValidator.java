package backingform.validator;

import backingform.AdverseEventBackingForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AdverseEventBackingFormValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return AdverseEventBackingForm.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    if (target == null) {
      return;
    }

    AdverseEventBackingForm adverseEventBackingForm = (AdverseEventBackingForm) target;

    if (adverseEventBackingForm.getType() == null) {
      errors.rejectValue("type", "adverseEvent.type.required", "Adverse event type is required");
    }
  }

}

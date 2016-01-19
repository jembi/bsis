package backingform.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import backingform.AdverseEventBackingForm;

@Component
public class AdverseEventBackingFormValidator extends BaseValidator<AdverseEventBackingForm> {

    @Override
    public void validateForm(AdverseEventBackingForm adverseEventBackingForm, Errors errors) {        
        if (adverseEventBackingForm.getType() == null) {
            errors.rejectValue("type", "adverseEvent.type.required", "Adverse event type is required");
        }
    }

    @Override
    public String getFormName() {
      return "adverseEvent";
    }
}

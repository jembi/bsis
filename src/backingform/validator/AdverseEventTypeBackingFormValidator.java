package backingform.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import repository.AdverseEventTypeRepository;
import backingform.AdverseEventTypeBackingForm;

@Component
public class AdverseEventTypeBackingFormValidator extends BaseValidator<AdverseEventTypeBackingForm> {
    
    @Autowired
    private AdverseEventTypeRepository adverseEventTypeRepository;

    @Override
    public void validateForm(AdverseEventTypeBackingForm adverseEventTypeBackingForm, Errors errors) {
        List<Long> existingAdverseEventTypeIds = adverseEventTypeRepository.findIdsByName(adverseEventTypeBackingForm.getName());
        for (Long id : existingAdverseEventTypeIds) {
            if (!id.equals(adverseEventTypeBackingForm.getId())) {
                errors.rejectValue("name", "adverseEventType.name.duplicate",
                        "There is already an adverse event type with that name");
                break;
            }
        }
    }

}

package backingform.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import repository.AdverseEventTypeRepository;
import backingform.AdverseEventTypeBackingForm;

@Component
public class AdverseEventTypeBackingFormValidator implements Validator {
    
    @Autowired
    private AdverseEventTypeRepository adverseEventTypeRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return AdverseEventTypeBackingForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target == null) {
            return;
        }

        AdverseEventTypeBackingForm adverseEventTypeBackingForm = (AdverseEventTypeBackingForm) target;

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

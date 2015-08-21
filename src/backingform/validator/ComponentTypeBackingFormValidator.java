package backingform.validator;


import backingform.ComponentTypeBackingForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;



import java.util.Arrays;

public class ComponentTypeBackingFormValidator  implements Validator {

    private Validator validator;

    public ComponentTypeBackingFormValidator(Validator validator){
        super();
        this.validator = validator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Arrays.asList(ComponentTypeBackingForm.class).contains(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors){

        ValidationUtils.invokeValidator(validator, obj, errors);


        ValidationUtils.rejectIfEmpty(errors, "componentType.componentTypeName", "componentTypeName.empty", "The componentTypeName is required");
        ValidationUtils.rejectIfEmpty(errors, "componentType.componentTypeNameShort", "componentTypeNameShort.empty", "The componentTypeNameShort is required");
        ValidationUtils.rejectIfEmpty(errors, "componentType.expiresAfter", "expiresAfter.empty", "The expiresAfter value is required");
        ValidationUtils.rejectIfEmpty(errors, "componentType.expiresAfterUnits", "expiresAfterUnits.empty", "The expiresAfterUnits value is required");
        ValidationUtils.rejectIfEmpty(errors, "componentType.description", "description.empty", "The description is required");

    }





}

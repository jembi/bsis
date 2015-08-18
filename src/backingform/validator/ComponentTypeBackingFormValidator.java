package backingform.validator;


import backingform.ComponentTypeBackingForm;
import controller.UtilController;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import repository.ProductTypeRepository;

import java.util.Arrays;

public class ComponentTypeBackingFormValidator  implements Validator {

    private Validator validator;
    private UtilController utilController;
    private ProductTypeRepository productTypeRepository;

    public ComponentTypeBackingFormValidator(Validator validator, UtilController utilController, ProductTypeRepository productTypeRepository){
        super();
        this.validator = validator;
        this.utilController = utilController;
        this.productTypeRepository = productTypeRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Arrays.asList(ComponentTypeBackingForm.class,
                ProductTypeRepository.class, String.class).contains(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors){

        ValidationUtils.invokeValidator(validator, obj, errors);
        ComponentTypeBackingForm form = (ComponentTypeBackingForm) obj;

        ValidationUtils.rejectIfEmpty(errors, "productType.productTypeName", "productTypeName.empty", "The productTypeName is required");
        ValidationUtils.rejectIfEmpty(errors, "productType.productTypeNameShort", "productTypeNameShort.empty", "The productTypeNameShort is required");
        ValidationUtils.rejectIfEmpty(errors, "productType.expiresAfter", "expiresAfter.empty", "The expiresAfter value is required");
        ValidationUtils.rejectIfEmpty(errors, "productType.expiresAfterUnits", "expiresAfterUnits.empty", "The expiresAfterUnits value is required");
        ValidationUtils.rejectIfEmpty(errors, "productType.description", "description.empty", "The description is required");

    }





}

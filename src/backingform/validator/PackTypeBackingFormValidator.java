package backingform.validator;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import model.packtype.PackType;
import repository.PackTypeRepository;
import viewmodel.PackTypeViewModel;
import backingform.PackTypeBackingForm;
import controller.UtilController;

public class PackTypeBackingFormValidator implements Validator {
	
	private Validator validator;
	private UtilController utilController;
	private PackTypeRepository packTypeRepository;
	
	public PackTypeBackingFormValidator(Validator validator, UtilController utilController,PackTypeRepository packTypeRepository) {
	    super();
	    this.validator = validator;
	    this.utilController = utilController;
	    this.packTypeRepository=packTypeRepository;
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
    
	    if (utilController.isDuplicatePackTypeName(form.getType())){
	    	errors.rejectValue("packType", "400",
	    	          "Pack Type name already exists.");
	    }
		
		
	}
}

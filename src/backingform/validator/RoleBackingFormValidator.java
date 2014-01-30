package backingform.validator;

import java.util.Arrays;

import model.collectedsample.CollectedSample;
import model.user.User;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import viewmodel.UserViewModel;
import backingform.CollectedSampleBackingForm;
import backingform.RoleBackingForm;
import backingform.UserBackingForm;
import controller.UtilController;

public class RoleBackingFormValidator implements Validator {

  private Validator validator;
  @SuppressWarnings("unused")
  private UtilController utilController;

  public RoleBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(UserBackingForm.class, User.class, UserViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
	  
	  RoleBackingForm form = (RoleBackingForm) obj;
	  
	  String roleName = form.getName();
	    if (StringUtils.isBlank(roleName))
	      return false;
	    CollectedSample existingCollection = collectedSampleRepository.findCollectionByCollectionNumberIncludeDeleted(collectionNumber);
	    if (existingCollection != null && !existingCollection.getId().equals(collection.getId()))
	      return true;
	    return false;
	  
	    errors.rejectValue("collectedSample.collectionNumber", "collectionNumber.nonunique",
	            "There exists a collection with the same collection number.");
	    
	  
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
  }
}

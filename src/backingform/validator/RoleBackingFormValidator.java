package backingform.validator;

import java.util.Arrays;
import java.util.Set;

import model.collectedsample.CollectedSample;
import model.user.Permission;
import model.user.Role;
import model.user.User;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import repository.RoleRepository;

import viewmodel.RoleViewModel;
import viewmodel.UserViewModel;
import backingform.CollectedSampleBackingForm;
import backingform.RoleBackingForm;
import backingform.UserBackingForm;
import controller.UtilController;

public class RoleBackingFormValidator implements Validator {

  private Validator validator;
  @SuppressWarnings("unused")
  private UtilController utilController;
  private RoleRepository roleRepository;

  public RoleBackingFormValidator(Validator validator, UtilController utilController) {
    super();
    this.validator = validator;
    this.utilController = utilController;
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean supports(Class<?> clazz) {
    return Arrays.asList(RoleBackingForm.class, Role.class, RoleViewModel.class).contains(clazz);
  }

  @Override
  public void validate(Object obj, Errors errors) {
	  
    if (obj == null || validator == null)
      return;
    ValidationUtils.invokeValidator(validator, obj, errors);
	  
	RoleBackingForm form = (RoleBackingForm) obj;  
	String roleName = form.getName();
	
	// Role name cannot be blank
    if (StringUtils.isBlank(roleName)){
    	errors.rejectValue("role.name", "roleName.empty",
	            "Role name cannot be empty.");
    }
    // Role name cannot exist already
    else{
	    Role existingRole = roleRepository.findRoleByName(roleName);
	    if(existingRole != null && !existingRole.getId().equals(form.getId())){
	    	errors.rejectValue("role.name", "roleName.nonunique",
		            "Role name already exists.");
	    }
    }
    
	Set<Permission> permissions = form.getPermissions();
	// permissions list cannot be empty
	if(permissions == null){
		errors.rejectValue("role.permissions", "permissions.empty",
	            "Role must have one or more permissions");
	}

	
  }
}

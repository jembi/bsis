package backingform.validator;

import java.util.Arrays;

import model.user.Role;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import repository.RoleRepository;
import viewmodel.RoleViewModel;
import backingform.RoleBackingForm;
import controller.UtilController;

public class RoleBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;
  private RoleRepository roleRepository;
  


public RoleBackingFormValidator(Validator validator, UtilController utilController,RoleRepository roleRepository) {
    super();
    this.validator = validator;
    this.utilController = utilController;
    this.roleRepository=roleRepository;
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
    
   	RoleBackingForm form = (RoleBackingForm) obj;  
	ValidationUtils.invokeValidator(validator, obj, errors);

	utilController.commonFieldChecks(form, "Role", errors);
	
	if(errors.hasErrors())
	return;
	
 	String roleName=form.getName();
	if(form.getPermissionValues()==null)
	{
    	errors.rejectValue("Role.permissions", "permissions.empty",
	            "Role must have one or more permissions");
        return;
	}

	 //  Role name cannot exist already
	  Role existingRole = roleRepository.findRoleByName(roleName);
         if(existingRole != null && !existingRole.getId().equals(form.getId())){
            errors.rejectValue("Role.name", "roleName.nonunique",
		            "Role name already exists.");
	    	return;
	    }
    
   }
}

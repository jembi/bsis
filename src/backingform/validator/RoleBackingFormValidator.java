package backingform.validator;

import model.user.Role;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import repository.RoleRepository;
import backingform.RoleBackingForm;

@Component
public class RoleBackingFormValidator extends BaseValidator<RoleBackingForm> {
  
  @Autowired
  private RoleRepository roleRepository;

  @Override
  public void validateForm(RoleBackingForm form, Errors errors) throws Exception {
    
    if (isDuplicateRoleName(form.getRole())){
    	errors.rejectValue("role.name", "roleName.nonunique",
    	          "Role name already exists.");
    }
    
	if(form.getPermissions().isEmpty()){
		errors.rejectValue("Role.permissions", "permissions.empty",
	            "Role must have one or more permissions");
	}
	
	commonFieldChecks(form, errors);
   }
  
  @Override
  public String getFormName() {
    return "Role";
  }
  
  private boolean isDuplicateRoleName(Role role) {
    String roleName = role.getName();
    if (StringUtils.isBlank(roleName)) {
      return false;
    }

    Role existingRole = roleRepository.findRoleByName(roleName);
    if (existingRole != null && !existingRole.getId().equals(role.getId())) {
      return true;
    }

    return false;
  }
}

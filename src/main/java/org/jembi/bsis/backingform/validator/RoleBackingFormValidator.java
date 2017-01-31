package org.jembi.bsis.backingform.validator;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.RoleBackingForm;
import org.jembi.bsis.model.user.Role;
import org.jembi.bsis.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class RoleBackingFormValidator extends BaseValidator<RoleBackingForm> {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public void validateForm(RoleBackingForm form, Errors errors) {

    if (isDuplicateRoleName(form)) {
      errors.rejectValue("name", "roleName.nonunique",
          "Role name already exists.");
    }

    if (form.getPermissions().isEmpty()) {
      errors.rejectValue("permissions", "permissions.empty",
          "Role must have one or more permissions");
    }

    commonFieldChecks(form, errors);
  }

  @Override
  public String getFormName() {
    return "Role";
  }

  private boolean isDuplicateRoleName(RoleBackingForm form) {
    String roleName = form.getName();
    if (StringUtils.isBlank(roleName)) {
      return false;
    }

    Role existingRole = roleRepository.findRoleByName(roleName);
    if (existingRole != null && !existingRole.getId().equals(form.getId())) {
      return true;
    }

    return false;
  }
}

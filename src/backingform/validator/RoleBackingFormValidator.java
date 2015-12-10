package backingform.validator;

import backingform.RoleBackingForm;
import controller.UtilController;
import model.user.Role;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import repository.RoleRepository;
import viewmodel.RoleViewModel;

import java.util.Arrays;

public class RoleBackingFormValidator implements Validator {

  private Validator validator;
  private UtilController utilController;
  private RoleRepository roleRepository;


  public RoleBackingFormValidator(Validator validator, UtilController utilController, RoleRepository roleRepository) {
    super();
    this.validator = validator;
    this.utilController = utilController;
    this.roleRepository = roleRepository;
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

    if (utilController.isDuplicateRoleName(form.getRole())) {
      errors.rejectValue("role.name", "roleName.nonunique",
              "Role name already exists.");
    }

    if (form.getPermissions().isEmpty()) {
      errors.rejectValue("Role.permissions", "permissions.empty",
              "Role must have one or more permissions");
    }

    utilController.commonFieldChecks(form, "Role", errors);

  }
}

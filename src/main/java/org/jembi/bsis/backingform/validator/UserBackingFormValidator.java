package org.jembi.bsis.backingform.validator;

import org.apache.commons.lang3.StringUtils;
import org.jembi.bsis.backingform.UserBackingForm;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.UserRepository;
import org.jembi.bsis.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class UserBackingFormValidator extends BaseValidator<UserBackingForm> {

  @Autowired
  private UserRepository userRepository;

  @Override
  public void validateForm(UserBackingForm form, Errors errors) {
    commonFieldChecks(form, errors);
    checkUserName(form, errors);
    if (form.getId() != null) {
      // Existing user, so check if password is being changed and validate it
      boolean skipCurrentPasswordCheck = form.getCurrentPassword() == null &&
          (canManageUsers() || isOwnPasswordReset(form));
      if (form.isModifyPassword() && (skipCurrentPasswordCheck || checkCurrentPassword(form, errors))) {
        comparePassword(form, errors);
      }
    } else {
      // New user, so always compare passwords
      comparePassword(form, errors);
    }

    checkRoles(form, errors);
  }

  @Override
  public String getFormName() {
    return "user";
  }

  private boolean checkCurrentPassword(UserBackingForm form, Errors errors) {

    if (form.getCurrentPassword() == null) {
      errors.rejectValue("password", "user.incorrect", "Current password is required");
      return false;
    }

    User user = userRepository.findUserById(form.getId());
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    boolean matches = encoder.matches(form.getCurrentPassword(), user.getPassword());
    if (!matches) {
      errors.rejectValue("password", "user.incorrect", "Current password does not match");
    }
    return matches;
  }

  private void comparePassword(UserBackingForm form, Errors errors) {
    if (StringUtils.isBlank(form.getPassword()) || StringUtils.isBlank(form.getConfirmPassword())) {
      errors.rejectValue("password", "user.incorrect", "Password cannot be blank");
    } else if (!form.getPassword().equals(form.getConfirmPassword())) {
      errors.rejectValue("password", "user.incorrect", "Passwords do not match");
    }

  }

  private void checkRoles(UserBackingForm form, Errors errors) {
    if (form.getRoles().isEmpty()) {
      errors.rejectValue("roles", "user.selectRole", "Must select at least one Role");
    }
  }

  private void checkUserName(UserBackingForm form, Errors errors) {

    boolean flag = false;
    String userName = form.getUsername();

    if (isDuplicateUserName(form)) {
      errors.rejectValue("username", "userName.nonunique", "Username already exists.");
    }

    if (userName.length() <= 2 || userName.length() >= 50) {
      flag = true;
    }

    if (!userName.matches("^[a-zA-Z0-9_.-]*$")) {
      flag = true;
    }

    if (flag && userName.length() > 0) {
      errors.rejectValue("username", "user.incorrect",
          "Username invalid. Use only alphanumeric characters, underscore (_), hyphen (-), and period (.).");
    }

  }

  /**
   * Check if the loggedOnUser is resetting their own password.
   */
  private boolean isOwnPasswordReset(UserBackingForm form) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return false;
    }
    User loggedOnUser = userRepository.findUser(authentication.getName());
    return loggedOnUser != null && loggedOnUser.isPasswordReset() && loggedOnUser.getId() == form.getId();
  }

  /**
   * Check if the logged on user has the authority to manage users.
   */
  private boolean canManageUsers() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return false;
    }
    for (GrantedAuthority authority : authentication.getAuthorities()) {
      if (PermissionConstants.MANAGE_USERS.equals(authority.getAuthority())) {
        return true;
      }
    }
    return false;
  }

  private boolean isDuplicateUserName(UserBackingForm form) {
    String userName = form.getUsername();
    if (StringUtils.isBlank(userName)) {
      return false;
    }

    User existingUser = userRepository.findUser(userName);
    if (existingUser != null && !existingUser.getId().equals(form.getId())) {
      return true;
    }

    return false;
  }
}

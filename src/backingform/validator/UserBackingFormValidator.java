package backingform.validator;

import java.util.Arrays;

import model.user.User;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import repository.UserRepository;
import security.UserAuthority;
import utils.PermissionConstants;
import viewmodel.UserViewModel;
import backingform.UserBackingForm;
import controller.UtilController;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserBackingFormValidator implements Validator {

    private Validator validator;
    private UtilController utilController;
    private UserRepository userRepository;

    public UserBackingFormValidator(Validator validator, UtilController utilController, UserRepository userRepository) {
        super();
        this.validator = validator;
        this.utilController = utilController;
        this.userRepository = userRepository;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean supports(Class<?> clazz) {
        return Arrays.asList(UserBackingForm.class, User.class, UserViewModel.class).contains(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        if (obj == null || validator == null) {
            return;
        }
        ValidationUtils.invokeValidator(validator, obj, errors);
        UserBackingForm form = (UserBackingForm) obj;
        utilController.commonFieldChecks(form, "user", errors);
        checkUserName(form, errors);
        if (form.getId() != null) {
            // Existing user, so check if password is being changed and validate it
            if (form.isModifyPassword() &&
                    (form.getCurrentPassword() == null && canManageUsers() || checkCurrentPassword(form, errors))) {
                comparePassword(form, errors);
            }
        } else {
            // New user, so always compare passwords
            comparePassword(form, errors);
        }

        checkRoles(form, errors);
    }
    
    private boolean checkCurrentPassword(UserBackingForm form, Errors errors) {
        User user = userRepository.findUserById(form.getId());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(form.getCurrentPassword(), user.getPassword());
        if (!matches) {
            errors.rejectValue("user.password", "user.incorrect", "Current password does not match");
        }
        return matches;
    }

    private void comparePassword(UserBackingForm form, Errors errors) {
        if (StringUtils.isBlank(form.getPassword()) || StringUtils.isBlank(form.getConfirmPassword())) {
            errors.rejectValue("user.password", "user.incorrect", "Password cannot be blank");
        } else if (!form.getPassword().equals(form.getConfirmPassword())) {
            errors.rejectValue("user.password", "user.incorrect", "Passwords do not match");
        }

    }

    private void checkRoles(UserBackingForm form, Errors errors) {
        if (form.getRoles().isEmpty()) {
            errors.rejectValue("user.roles", "user.selectRole", "Must select at least one Role");
        }
    }

    private void checkUserName(UserBackingForm form, Errors errors) {

        boolean flag = false;
        String userName = form.getUser().getUsername();
        User existingUser = null;

        if (utilController.isDuplicateUserName(form.getUser())) {
            errors.rejectValue("user.username", "userName.nonunique", "Username already exists.");
        }

        if (userName.length() <= 2 || userName.length() >= 50) {
            flag = true;
        }

        if (!userName.matches("^[a-zA-Z0-9_.-]*$")) {
            flag = true;
        }

        if (flag && userName.length() > 0) {
            errors.rejectValue("user.username", "user.incorrect",
                    "Username invalid. Use only alphanumeric characters, underscore (_), hyphen (-), and period (.).");
        }

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
}

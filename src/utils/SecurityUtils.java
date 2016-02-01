package utils;

import model.user.User;

import org.springframework.security.core.context.SecurityContextHolder;

import security.BsisUserDetails;

/**
 * Utility class for methods related to security
 */
public class SecurityUtils {

  /**
   * Find the currently authenticated user
   * 
   * @return User that is logged in, null if no user can be found
   */
  public static User getCurrentUser() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = null;
    if (principal != null && principal instanceof BsisUserDetails)
      user = ((BsisUserDetails) principal).getUser();
    return user;
  }
}

package org.jembi.bsis.utils;

import org.jembi.bsis.model.user.User;
import org.jembi.bsis.security.BsisUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

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

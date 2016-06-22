package org.jembi.bsis.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class PermissionUtils {

  public static boolean loggedOnUserHasPermission(String permission) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return false;
    }
    for (GrantedAuthority authority : authentication.getAuthorities()) {
      if (permission.equals(authority.getAuthority())) {
        return true;
      }
    }
    return false;
  }

}

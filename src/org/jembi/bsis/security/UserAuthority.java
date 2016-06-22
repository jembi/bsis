package org.jembi.bsis.security;

import org.jembi.bsis.model.user.Permission;
import org.springframework.security.core.GrantedAuthority;

public class UserAuthority implements GrantedAuthority {

  /**
   *
   */
  private static final long serialVersionUID = 1L;

  private Permission permission;

  public UserAuthority(Permission permission) {
    this.permission = permission;
  }

  @Override
  public String getAuthority() {
    return permission.getName();
  }

  @Override
  public String toString() {
    return permission.getName();
  }
}

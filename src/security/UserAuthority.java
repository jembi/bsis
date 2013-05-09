package security;

import model.user.Role;

import org.springframework.security.core.GrantedAuthority;

public class UserAuthority implements GrantedAuthority {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private Role role;

  public UserAuthority(Role role) {
    this.role = role;
  }

  @Override
  public String getAuthority() {
    return role.getName();
  }

}

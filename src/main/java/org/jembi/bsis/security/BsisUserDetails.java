package org.jembi.bsis.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jembi.bsis.model.user.Permission;
import org.jembi.bsis.model.user.Role;
import org.jembi.bsis.model.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class BsisUserDetails implements UserDetails {

  private static final long serialVersionUID = 1L;
  private User user;

  public BsisUserDetails(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<UserAuthority> userAuthorities = new ArrayList<UserAuthority>();
    /**
     * Reason for using Permission as Authority object
     * http://stackoverflow.com/questions/6357579/spring-security-with-roles-and-permissions
     */
    for (Role role : user.getRoles()) {
      for (Permission permission : role.getPermissions()) {
        userAuthorities.add(new UserAuthority(permission));
      }
    }
    return userAuthorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return user.getIsActive();
  }

  public User getUser() {
    return user;
  }
}

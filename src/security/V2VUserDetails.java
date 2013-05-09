package security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import model.user.Role;
import model.user.User;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class V2VUserDetails implements UserDetails {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private User user;

  public V2VUserDetails(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<UserAuthority> userAuthorities = new ArrayList<UserAuthority>();
    for (Role role : user.getRoles()) {
      userAuthorities.add(new UserAuthority(role));
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
}

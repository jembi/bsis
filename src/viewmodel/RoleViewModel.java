package viewmodel;

import model.user.Permission;
import model.user.Role;

import java.util.Set;

public class RoleViewModel {

  private Role role;

  public RoleViewModel() {
  }

  public RoleViewModel(Role role) {
    this.role = role;
  }

  public Long getId() {
    return role.getId();
  }

  public String getName() {
    return role.getName();
  }

  public Set<Permission> getPermissions() {
    return role.getPermissions();
  }

  public String getDescription() {
    return role.getDescription();
  }

}

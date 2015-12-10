package backingform;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.user.Permission;
import model.user.Role;
import model.user.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

public class RoleBackingForm {

  @Valid
  @JsonIgnore
  private Role role;

  public RoleBackingForm() {
    setRole(new Role());
  }

  public RoleBackingForm(Role role) {
    this.setRole(role);
  }

  public boolean equals(Object obj) {
    return role.equals(obj);
  }

  public Long getId() {
    return role.getId();
  }

  public void setId(Long id) {
    role.setId(id);
  }

  public String getName() {
    return role.getName();
  }

  public void setName(String name) {
    role.setName(name);
  }

  @JsonIgnore
  public List<User> getUsers() {
    return role.getUsers();
  }

  public void setUsers(List<User> users) {
    role.setUsers(users);
  }

  public Set<Permission> getPermissions() {
    return role.getPermissions();
  }

  public void setPermissions(Set<Permission> permissions) {
    role.setPermissions(permissions);
  }

  public String getDescription() {
    return role.getDescription();
  }

  public void setDescription(String description) {
    role.setDescription(description);
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

}

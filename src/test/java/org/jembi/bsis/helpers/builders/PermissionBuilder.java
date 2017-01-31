package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.user.Permission;
import org.jembi.bsis.model.user.Role;

public class PermissionBuilder extends AbstractEntityBuilder<Permission> {

  private Long id;
  private String name;
  private List<Role> roles;

  public PermissionBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public PermissionBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public PermissionBuilder withRoles(List<Role> roles) {
    this.roles = roles;
    return this;
  }

  public PermissionBuilder withRole(Role role) {
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    this.roles.add(role);
    return this;
  }

  @Override
  public Permission build() {
    Permission permission = new Permission();
    permission.setId(id);
    permission.setName(name);
    permission.setRoles(roles);
    return permission;
  }

  public static PermissionBuilder aPermission() {
    return new PermissionBuilder();
  }
}

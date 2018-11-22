package org.jembi.bsis.helpers.builders;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.model.user.Permission;
import org.jembi.bsis.model.user.Role;

public class RoleBuilder extends AbstractEntityBuilder<Role> {

  private UUID id;
  private String name;
  private String description;
  private Set<Permission> permissions;

  public RoleBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public RoleBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public RoleBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  public RoleBuilder withPermissions(Set<Permission> permissions) {
    this.permissions = permissions;
    return this;
  }

  public RoleBuilder withPermission(Permission permission) {
    if (this.permissions == null) {
      this.permissions = new HashSet<>();
    }
    this.permissions.add(permission);
    return this;
  }

  @Override
  public Role build() {
    Role role = new Role();
    role.setId(id);
    role.setName(name);
    role.setDescription(description);
    role.setPermissions(permissions);
    return role;
  }

  public static RoleBuilder aRole() {
    return new RoleBuilder();
  }
}

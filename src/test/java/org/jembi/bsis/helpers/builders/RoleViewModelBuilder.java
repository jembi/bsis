package org.jembi.bsis.helpers.builders;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.viewmodel.PermissionViewModel;
import org.jembi.bsis.viewmodel.RoleViewModel;

public class RoleViewModelBuilder extends AbstractBuilder<RoleViewModel> {

  private UUID id;
  private String name;
  private String description;
  private Set<PermissionViewModel> permissions;

  public RoleViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public RoleViewModelBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public RoleViewModelBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  public RoleViewModelBuilder withPermissions(Set<PermissionViewModel> permissions) {
    this.permissions = permissions;
    return this;
  }

  public RoleViewModelBuilder withPermission(PermissionViewModel permission) {
    if (this.permissions == null) {
      this.permissions = new HashSet<>();
    }
    this.permissions.add(permission);
    return this;
  }

  @Override
  public RoleViewModel build() {
    RoleViewModel role = new RoleViewModel();
    role.setId(id);
    role.setName(name);
    role.setDescription(description);
    role.setPermissions(permissions);
    return role;
  }

  public static RoleViewModelBuilder aRoleViewModel() {
    return new RoleViewModelBuilder();
  }
}

package org.jembi.bsis.helpers.builders;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.backingform.PermissionBackingForm;
import org.jembi.bsis.backingform.RoleBackingForm;

public class RoleBackingFormBuilder extends AbstractBuilder<RoleBackingForm> {

  private UUID id;
  private String name;
  private String description;
  private Set<PermissionBackingForm> permissions;

  public RoleBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public RoleBackingFormBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public RoleBackingFormBuilder withDescription(String description) {
    this.description = description;
    return this;
  }

  public RoleBackingFormBuilder withPermissions(Set<PermissionBackingForm> permissions) {
    this.permissions = permissions;
    return this;
  }

  public RoleBackingFormBuilder withPermission(PermissionBackingForm permission) {
    if (this.permissions == null) {
      this.permissions = new HashSet<>();
    }
    this.permissions.add(permission);
    return this;
  }

  @Override
  public RoleBackingForm build() {
    RoleBackingForm roleBackingForm = new RoleBackingForm();
    roleBackingForm.setId(id);
    roleBackingForm.setName(name);
    roleBackingForm.setDescription(description);
    roleBackingForm.setPermissions(permissions);
    return roleBackingForm;
  }

  public static RoleBackingFormBuilder aRoleBackingForm() {
    return new RoleBackingFormBuilder();
  }
}

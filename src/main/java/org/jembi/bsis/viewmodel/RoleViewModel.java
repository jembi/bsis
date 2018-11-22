package org.jembi.bsis.viewmodel;

import java.util.Set;
import java.util.UUID;

public class RoleViewModel extends BaseViewModel<UUID> {

  private String name;
  private String description;
  private Set<PermissionViewModel> permissions;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Set<PermissionViewModel> getPermissions() {
    return permissions;
  }

  public void setPermissions(Set<PermissionViewModel> permissions) {
    this.permissions = permissions;
  }
}
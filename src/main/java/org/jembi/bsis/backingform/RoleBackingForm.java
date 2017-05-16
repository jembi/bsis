package org.jembi.bsis.backingform;

import java.util.Set;
import java.util.UUID;

public class RoleBackingForm {

  private UUID id;
  private String name;
  private String description;
  private Set<PermissionBackingForm> permissions;
  
  public String getDescription() {
    return description;
  }

  public UUID getId() {
    return id;
  }

  public Set<PermissionBackingForm> getPermissions() {
    return permissions;
  }

  public String getName() {
    return name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPermissions(Set<PermissionBackingForm> permissions) {
    this.permissions = permissions;
  }

}

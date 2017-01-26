package org.jembi.bsis.backingform;

import java.util.Set;

public class RoleBackingForm {

  private Long id;
  private String name;
  private String description;
  private Set<PermissionBackingForm> permissions;
  
  public String getDescription() {
    return description;
  }

  public Long getId() {
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

  public void setId(Long id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPermissions(Set<PermissionBackingForm> permissions) {
    this.permissions = permissions;
  }

}


package org.jembi.bsis.backingform;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DivisionBackingForm {

  private UUID id;

  private String name;

  private Integer level;

  private DivisionBackingForm parent;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public DivisionBackingForm getParent() {
    return parent;
  }

  public void setParent(DivisionBackingForm parent) {
    this.parent = parent;
  }
  
  @JsonIgnore
  public void setPermissions(Map<String, Boolean> permissions) {
    // Ignore field from view model
  }
  
}

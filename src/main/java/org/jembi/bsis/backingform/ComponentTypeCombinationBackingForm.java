package org.jembi.bsis.backingform;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ComponentTypeCombinationBackingForm {

  private UUID id;

  private String combinationName;

  private List<ComponentTypeBackingForm> componentTypes;

  private Set<ComponentTypeBackingForm> sourceComponentTypes;

  private Boolean isDeleted;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public List<ComponentTypeBackingForm> getComponentTypes() {
    return componentTypes;
  }

  public void setComponentTypes(List<ComponentTypeBackingForm> componentTypes) {
    this.componentTypes = componentTypes;
  }

  public Set<ComponentTypeBackingForm> getSourceComponentTypes() {
    return sourceComponentTypes;
  }

  public void setSourceComponentTypes(Set<ComponentTypeBackingForm> sourceComponentTypes) {
    this.sourceComponentTypes = sourceComponentTypes;
  }

  public String getCombinationName() {
    return combinationName;
  }

  public void setCombinationName(String combinationName) {
    this.combinationName = combinationName;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  @JsonIgnore
  public void setPermissions(Map<String, Boolean> permissions) {
    // Ignore field from view model
  }
}

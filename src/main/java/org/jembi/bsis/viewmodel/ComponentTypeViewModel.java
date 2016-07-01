package org.jembi.bsis.viewmodel;

import org.jembi.bsis.model.componenttype.ComponentType;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ComponentTypeViewModel extends BaseViewModel {

  public ComponentTypeViewModel(ComponentType componentType) {
    this.componentType = componentType;
  }

  @JsonIgnore
  ComponentType componentType;

  public ComponentType getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentType componentType) {
    this.componentType = componentType;
  }

  @Override
  public Long getId() {
    return componentType.getId();
  }

  public String getComponentTypeName() {
    return componentType.getComponentTypeName();
  }

  public String getComponentTypeCode() {
    return componentType.getComponentTypeCode();
  }

  public String getDescription() {
    return componentType.getDescription();
  }
}

package viewmodel;

import model.componenttype.ComponentType;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ComponentTypeViewModel {

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

  public Long getId() {
    return componentType.getId();
  }

  public String getComponentTypeName() {
    return componentType.getComponentTypeName();
  }

  public String getComponentTypeNameShort() {
    return componentType.getComponentTypeNameShort();
  }

  public String getDescription() {
    return componentType.getDescription();
  }
}

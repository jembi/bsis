package org.jembi.bsis.viewmodel;

import java.util.List;

public class ComponentTypeCombinationViewModel {
  
  private Long id;
  private String combinationName;
  List<ComponentTypeViewModel> componentTypes;
  
  public Long getId() {
    return this.id;
  }

  public String getCombinationName() {
    return this.combinationName;
  }

  public List<ComponentTypeViewModel> getComponentTypes() {
    return componentTypes;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCombinationName(String combinationName) {
    this.combinationName = combinationName;
  }
  
  public void setComponentTypes(List<ComponentTypeViewModel> componentTypes) {
    this.componentTypes = componentTypes;
  }
  
}

package org.jembi.bsis.viewmodel;

import java.util.List;

import org.jembi.bsis.model.componenttype.ComponentType;

public class ComponentTypeCombinationViewModel {
  
  private Long id;
  private String combinationName;
  List<ComponentType> componentTypes;
  
  public Long getId() {
    return this.id;
  }

  public String getCombinationName() {
    return this.combinationName;
  }

  public List<ComponentType> getComponentTypes() {
    // FIXME: use factory
    return componentTypes;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCombinationName(String combinationName) {
    this.combinationName = combinationName;
  }

  public void setComponentTypes(List<ComponentType> componentTypes) {
    this.componentTypes = componentTypes;
  }
  
  /*
   * public List<ComponentTypeViewModel> getComponentTypes() { // FIXME: use factory return
   * getComponentTypeViewModels(componentTypeCombination.getComponentTypes()); }
   * 
   * private List<ComponentTypeViewModel> getComponentTypeViewModels(List<ComponentType>
   * componentTypes) { List<ComponentTypeViewModel> componentTypeViewModels = new
   * ArrayList<ComponentTypeViewModel>(); for (ComponentType componentType : componentTypes) {
   * componentTypeViewModels.add(new ComponentTypeViewModel(componentType)); } return
   * componentTypeViewModels; }
   */
}

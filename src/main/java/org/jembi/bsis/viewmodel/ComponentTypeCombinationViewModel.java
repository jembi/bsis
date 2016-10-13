package org.jembi.bsis.viewmodel;

import java.util.List;

public class ComponentTypeCombinationViewModel extends ComponentTypeCombinationSearchViewModel {

  List<ComponentTypeViewModel> componentTypes;

  public List<ComponentTypeViewModel> getComponentTypes() {
    return componentTypes;
  }
  
  public void setComponentTypes(List<ComponentTypeViewModel> componentTypes) {
    this.componentTypes = componentTypes;
  }
  
}

package org.jembi.bsis.viewmodel;

import java.util.List;
import java.util.Set;

public class ComponentTypeCombinationFullViewModel extends ComponentTypeCombinationViewModel {

  private Set<ComponentTypeViewModel> sourceComponentTypes;
  private List<ComponentTypeViewModel> componentTypes;

  public Set<ComponentTypeViewModel> getSourceComponentTypes() {
    return sourceComponentTypes;
  }

  public void setSourceComponentTypes(Set<ComponentTypeViewModel> sourceComponentTypes) {
    this.sourceComponentTypes = sourceComponentTypes;
  }

  public List<ComponentTypeViewModel> getComponentTypes() {
    return componentTypes;
  }

  public void setComponentTypes(List<ComponentTypeViewModel> componentTypes) {
    this.componentTypes = componentTypes;
  }
}

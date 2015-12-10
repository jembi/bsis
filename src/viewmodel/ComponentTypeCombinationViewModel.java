package viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.componenttype.ComponentType;
import model.componenttype.ComponentTypeCombination;

import java.util.ArrayList;
import java.util.List;

public class ComponentTypeCombinationViewModel {

  @JsonIgnore
  private ComponentTypeCombination componentTypeCombination;

  public ComponentTypeCombinationViewModel(ComponentTypeCombination componentTypeCombination) {
    this.componentTypeCombination = componentTypeCombination;
  }

  public ComponentTypeCombination getComponentTypeCombination() {
    return componentTypeCombination;
  }

  public void setComponentTypeCombination(ComponentTypeCombination componentTypeCombination) {
    this.componentTypeCombination = componentTypeCombination;
  }

  public Integer getId() {
    return componentTypeCombination.getId();
  }

  public String getCombinationName() {
    return componentTypeCombination.getCombinationName();
  }

  public List<ComponentTypeViewModel> getComponentTypes() {
    return getComponentTypeViewModels(componentTypeCombination.getComponentTypes());
  }

  private List<ComponentTypeViewModel> getComponentTypeViewModels(List<ComponentType> componentTypes) {
    List<ComponentTypeViewModel> componentTypeViewModels = new ArrayList<>();
    for (ComponentType componentType : componentTypes) {
      componentTypeViewModels.add(new ComponentTypeViewModel(componentType));
    }
    return componentTypeViewModels;
  }

}

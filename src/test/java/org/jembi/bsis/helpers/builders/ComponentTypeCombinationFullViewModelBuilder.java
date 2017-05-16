package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.viewmodel.ComponentTypeCombinationFullViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;

public class ComponentTypeCombinationFullViewModelBuilder extends AbstractBuilder<ComponentTypeCombinationFullViewModel> {
  
  private UUID id;
  private boolean isDeleted = false;
  private String combinationName;
  private List<ComponentTypeViewModel> componentTypes;
  private Set<ComponentTypeViewModel> sourceComponentTypes;

  public ComponentTypeCombinationFullViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public ComponentTypeCombinationFullViewModelBuilder withCombinationName(String combinationName) {
    this.combinationName = combinationName;
    return this;
  }
  
  public ComponentTypeCombinationFullViewModelBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  public ComponentTypeCombinationFullViewModelBuilder withComponentTypes(List<ComponentTypeViewModel> componentTypes) {
    this.componentTypes = componentTypes;
    return this;
  }

  public ComponentTypeCombinationFullViewModelBuilder withComponentType(ComponentTypeViewModel componentType) {
    if (this.componentTypes == null) {
      this.componentTypes = new ArrayList<>();
    }
    this.componentTypes.add(componentType);
    return this;
  }

  public ComponentTypeCombinationFullViewModelBuilder withSourceComponentTypes(Set<ComponentTypeViewModel> sourceComponentTypes) {
    this.sourceComponentTypes = sourceComponentTypes;
    return this;
  }

  public ComponentTypeCombinationFullViewModelBuilder withSourceComponentType(ComponentTypeViewModel sourceComponentType) {
    if (this.sourceComponentTypes == null) {
      this.sourceComponentTypes = new HashSet<>();
    }
    this.sourceComponentTypes.add(sourceComponentType);
    return this;
  }
  
  @Override
  public ComponentTypeCombinationFullViewModel build() {
    ComponentTypeCombinationFullViewModel viewModel = new ComponentTypeCombinationFullViewModel();
    viewModel.setId(id);
    viewModel.setCombinationName(combinationName);
    viewModel.setIsDeleted(isDeleted);
    viewModel.setComponentTypes(componentTypes);
    viewModel.setSourceComponentTypes(sourceComponentTypes);
    return viewModel;
  }
  
  public static ComponentTypeCombinationFullViewModelBuilder aComponentTypeCombinationFullViewModel() {
    return new ComponentTypeCombinationFullViewModelBuilder();
  }
}

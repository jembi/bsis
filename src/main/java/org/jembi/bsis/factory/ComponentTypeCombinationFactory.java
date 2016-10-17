package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationFullViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentTypeCombinationFactory {

  @Autowired
  private ComponentTypeFactory componentTypeFactory;

  public List<ComponentTypeCombinationViewModel> createViewModels(List<ComponentTypeCombination> componentTypeCombinations) {
    List<ComponentTypeCombinationViewModel> combinationViewModels = new ArrayList<>();
    if (componentTypeCombinations != null) {
      for (ComponentTypeCombination componentTypeCombination : componentTypeCombinations) {
        combinationViewModels.add(createViewModel(componentTypeCombination));
      }
    }
    return combinationViewModels;
  }

  public ComponentTypeCombinationViewModel createViewModel(ComponentTypeCombination componentTypeCombination) {
    ComponentTypeCombinationViewModel viewModel = new ComponentTypeCombinationViewModel();
    if (componentTypeCombination != null) {
      populateComponentTypeCombination(viewModel, componentTypeCombination);
    }
    return viewModel;
  }

  public ComponentTypeCombinationFullViewModel createFullViewModel(ComponentTypeCombination componentTypeCombination) {
    ComponentTypeCombinationFullViewModel viewModel = new ComponentTypeCombinationFullViewModel();
    if (componentTypeCombination != null) {
      populateComponentTypeCombination(viewModel, componentTypeCombination);
      populateComponentTypeCombinationFull(viewModel, componentTypeCombination);
    }
    return viewModel;
  }

  private void populateComponentTypeCombination(ComponentTypeCombinationViewModel viewModel,
      ComponentTypeCombination componentTypeCombination) {
    viewModel.setId(componentTypeCombination.getId());
    viewModel.setCombinationName(componentTypeCombination.getCombinationName());
    viewModel.setIsDeleted(componentTypeCombination.getIsDeleted());
  }

  private void populateComponentTypeCombinationFull(ComponentTypeCombinationFullViewModel viewModel,
      ComponentTypeCombination componentTypeCombination) {
    // set produced component types (a list)
    viewModel.setComponentTypes(componentTypeFactory.createViewModels(componentTypeCombination.getComponentTypes()));
    // set source component types (a set)
    List<ComponentType> sourceComponentTypes = new ArrayList<>(componentTypeCombination.getSourceComponentTypes()); 
    List<ComponentTypeViewModel> sourceComponentTypeViewModels = componentTypeFactory.createViewModels(sourceComponentTypes);
    viewModel.setSourceComponentTypes(new HashSet<>(sourceComponentTypeViewModels));
  }
}

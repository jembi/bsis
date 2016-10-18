package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;
import org.springframework.stereotype.Service;

@Service
public class ComponentTypeCombinationFactory {

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
      viewModel.setId(componentTypeCombination.getId());
      viewModel.setCombinationName(componentTypeCombination.getCombinationName());
      viewModel.setIsDeleted(componentTypeCombination.getIsDeleted());
    }
    return viewModel;
  }

}

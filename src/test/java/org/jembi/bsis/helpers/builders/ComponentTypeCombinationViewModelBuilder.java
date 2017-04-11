package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;

public class ComponentTypeCombinationViewModelBuilder extends AbstractBuilder<ComponentTypeCombinationViewModel> {
  
  private UUID id;
  private boolean isDeleted = false;
  private String combinationName;

  public ComponentTypeCombinationViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public ComponentTypeCombinationViewModelBuilder withCombinationName(String combinationName) {
    this.combinationName = combinationName;
    return this;
  }
  
  public ComponentTypeCombinationViewModelBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }
  
  @Override
  public ComponentTypeCombinationViewModel build() {
    ComponentTypeCombinationViewModel viewModel = new ComponentTypeCombinationViewModel();
    viewModel.setId(id);
    viewModel.setCombinationName(combinationName);
    viewModel.setIsDeleted(isDeleted);
    return viewModel;
  }
  
  public static ComponentTypeCombinationViewModelBuilder aComponentTypeCombinationViewModel() {
    return new ComponentTypeCombinationViewModelBuilder();
  }
}

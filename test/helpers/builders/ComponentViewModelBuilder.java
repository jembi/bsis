package helpers.builders;

import viewmodel.ComponentViewModel;

public class ComponentViewModelBuilder extends AbstractBuilder<ComponentViewModel> {

  @Override
  public ComponentViewModel build() {
    ComponentViewModel viewModel = new ComponentViewModel();
    return viewModel;
  }
  
  public static ComponentViewModelBuilder aComponentViewModel() {
    return new ComponentViewModelBuilder();
  }

}

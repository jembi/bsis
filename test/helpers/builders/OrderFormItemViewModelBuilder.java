package helpers.builders;

import model.componenttype.ComponentType;
import viewmodel.ComponentTypeViewModel;
import viewmodel.OrderFormItemViewModel;

public class OrderFormItemViewModelBuilder extends AbstractBuilder<OrderFormItemViewModel> {

  private Long id;
  private ComponentTypeViewModel componentType;
  private String bloodAbo;
  private String bloodRh;
  private int numberOfUnits;

  public OrderFormItemViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public OrderFormItemViewModelBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }
  
  public OrderFormItemViewModelBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  public OrderFormItemViewModelBuilder withNumberOfUnits(int numberOfUnits) {
    this.numberOfUnits = numberOfUnits;
    return this;
  }

  public OrderFormItemViewModelBuilder withComponentType(ComponentTypeViewModel componentType) {
    this.componentType = componentType;
    return this;
  }
  
  public OrderFormItemViewModelBuilder withComponentType(ComponentType componentType) {
    this.componentType = new ComponentTypeViewModel(componentType);
    return this;
  }

  public OrderFormItemViewModel build() {
    OrderFormItemViewModel viewModel = new OrderFormItemViewModel();
    viewModel.setId(id);
    viewModel.setComponentType(componentType);
    viewModel.setBloodAbo(bloodAbo);
    viewModel.setBloodRh(bloodRh);
    viewModel.setNumberOfUnits(numberOfUnits);
    return viewModel;
  }

  public static OrderFormItemViewModelBuilder anOrderFormItemViewModel() {
    return new OrderFormItemViewModelBuilder();
  }

}

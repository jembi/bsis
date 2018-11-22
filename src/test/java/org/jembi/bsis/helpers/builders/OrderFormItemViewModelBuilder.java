package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.OrderFormItemViewModel;

public class OrderFormItemViewModelBuilder extends AbstractBuilder<OrderFormItemViewModel> {

  private UUID id;
  private ComponentTypeViewModel componentType;
  private String bloodGroup;
  private int numberOfUnits;

  public OrderFormItemViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public OrderFormItemViewModelBuilder withBloodGroup(String bloodGroup) {
    this.bloodGroup = bloodGroup;
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

  public OrderFormItemViewModel build() {
    OrderFormItemViewModel viewModel = new OrderFormItemViewModel();
    viewModel.setId(id);
    viewModel.setComponentType(componentType);
    viewModel.setBloodGroup(bloodGroup);
    viewModel.setNumberOfUnits(numberOfUnits);
    return viewModel;
  }

  public static OrderFormItemViewModelBuilder anOrderFormItemViewModel() {
    return new OrderFormItemViewModelBuilder();
  }

}

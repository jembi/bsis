package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.ComponentViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;

public class ComponentViewModelBuilder extends AbstractBuilder<ComponentViewModel> {

  private Long id;
  private LocationFullViewModel location;
  private InventoryStatus inventoryStatus;
  private ComponentStatus status;
  private String bloodRh;
  private String bloodAbo;
  private ComponentTypeViewModel componentType;

  public ComponentViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ComponentViewModelBuilder withLocation(LocationFullViewModel location) {
    this.location = location;
    return this;
  }

  public ComponentViewModelBuilder withInventoryStatus(InventoryStatus inventoryStatus) {
    this.inventoryStatus = inventoryStatus;
    return this;
  }

  public ComponentViewModelBuilder withStatus(ComponentStatus status) {
    this.status = status;
    return this;
  }

  public ComponentViewModelBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }

  public ComponentViewModelBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  public ComponentViewModelBuilder withComponentType(ComponentTypeViewModel componentType) {
    this.componentType = componentType;
    return this;
  }

  @Override
  public ComponentViewModel build() {
    ComponentViewModel viewModel = new ComponentViewModel();
    viewModel.setId(id);
    viewModel.setInventoryStatus(inventoryStatus);
    viewModel.setStatus(status);
    viewModel.setLocation(location);
    viewModel.setBloodAbo(bloodAbo);
    viewModel.setBloodRh(bloodRh);
    viewModel.setComponentType(componentType);
    return viewModel;
  }
  
  public static ComponentViewModelBuilder aComponentViewModel() {
    return new ComponentViewModelBuilder();
  }

}

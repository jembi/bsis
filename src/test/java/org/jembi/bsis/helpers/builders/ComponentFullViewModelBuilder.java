package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.viewmodel.ComponentFullViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;

public class ComponentFullViewModelBuilder extends AbstractBuilder<ComponentFullViewModel> {

  private UUID id;
  private LocationViewModel location;
  private InventoryStatus inventoryStatus;
  private ComponentStatus status;
  private Date createdOn;
  private Date expiresOn;
  private int daysToExpire;
  private String bloodRh;
  private String bloodAbo;
  private ComponentTypeViewModel componentType;
  private boolean isInitialComponent = false;

  public ComponentFullViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public ComponentFullViewModelBuilder withLocation(LocationViewModel location) {
    this.location = location;
    return this;
  }

  public ComponentFullViewModelBuilder withInventoryStatus(InventoryStatus inventoryStatus) {
    this.inventoryStatus = inventoryStatus;
    return this;
  }

  public ComponentFullViewModelBuilder withStatus(ComponentStatus status) {
    this.status = status;
    return this;
  }
  public ComponentFullViewModelBuilder withCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
    return this;
  }

  public ComponentFullViewModelBuilder withExpiresOn(Date expiresOn) {
    this.expiresOn = expiresOn;
    return this;
  }

  public ComponentFullViewModelBuilder withDaysToExpire(int daysToExpire) {
    this.daysToExpire = daysToExpire;
    return this;
  }

  public ComponentFullViewModelBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }

  public ComponentFullViewModelBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  public ComponentFullViewModelBuilder withComponentType(ComponentTypeViewModel componentType) {
    this.componentType = componentType;
    return this;
  }
  
  public ComponentFullViewModelBuilder thatIsInitialComponent () {
    this.isInitialComponent = true;
    return this;
  }
  
  public ComponentFullViewModelBuilder thatIsNotInitialComponent () {
    this.isInitialComponent = false;
    return this;
  }

  @Override
  public ComponentFullViewModel build() {
    ComponentFullViewModel viewModel = new ComponentFullViewModel();
    viewModel.setId(id);
    viewModel.setInventoryStatus(inventoryStatus);
    viewModel.setStatus(status);
    viewModel.setCreatedOn(createdOn);
    viewModel.setExpiresOn(expiresOn);
    viewModel.setDaysToExpire(daysToExpire);
    viewModel.setLocation(location);
    viewModel.setBloodAbo(bloodAbo);
    viewModel.setBloodRh(bloodRh);
    viewModel.setComponentType(componentType);
    viewModel.setIsInitialComponent(isInitialComponent);
    return viewModel;
  }
  
  public static ComponentFullViewModelBuilder aComponentFullViewModel() {
    return new ComponentFullViewModelBuilder();
  }

}

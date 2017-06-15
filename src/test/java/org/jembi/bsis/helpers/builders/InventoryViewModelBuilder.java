package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.InventoryViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;

public class InventoryViewModelBuilder extends AbstractBuilder<InventoryViewModel> {

  private UUID id;
  private ComponentTypeViewModel componentType;
  private Date createdOn;
  private String donationIdentificationNumber;
  private InventoryStatus inventoryStatus;
  private int daysToExpire;
  private String componentCode;
  private Date expiresOn;
  private LocationViewModel location;
  private String bloodGroup;
  private ComponentStatus componentStatus;

  public InventoryViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public InventoryViewModelBuilder withInventoryStatus(InventoryStatus inventoryStatus) {
    this.inventoryStatus = inventoryStatus;
    return this;
  }

  public InventoryViewModelBuilder withComponentType(ComponentTypeViewModel componentType) {
    this.componentType = componentType;
    return this;
  }

  public InventoryViewModelBuilder withDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }

  public InventoryViewModelBuilder withCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
    return this;
  }

  public InventoryViewModelBuilder withDaysToExpire(int daysToExpire) {
    this.daysToExpire = daysToExpire;
    return this;
  }

  public InventoryViewModelBuilder withComponentCode(String componentCode) {
    this.componentCode = componentCode;
    return this;
  }

  public InventoryViewModelBuilder withExpiresOn(Date expiresOn) {
    this.expiresOn = expiresOn;
    return this;
  }

  public InventoryViewModelBuilder withLocation(LocationViewModel location) {
    this.location = location;
    return this;
  }

  public InventoryViewModelBuilder withBloodGroup(String bloodGroup) {
    this.bloodGroup = bloodGroup;
    return this;
  }

  public InventoryViewModelBuilder withComponentStatus(ComponentStatus componentStatus) {
    this.componentStatus = componentStatus;
    return this;
  }

  @Override
  public InventoryViewModel build() {
    InventoryViewModel viewModel = new InventoryViewModel();
    viewModel.setId(id);
    viewModel.setInventoryStatus(inventoryStatus);
    viewModel.setComponentType(componentType);
    viewModel.setCreatedOn(createdOn);
    viewModel.setDonationIdentificationNumber(donationIdentificationNumber);
    viewModel.setComponentCode(componentCode);
    viewModel.setDaysToExpire(daysToExpire);
    viewModel.setExpiresOn(expiresOn);
    viewModel.setLocation(location);
    viewModel.setBloodGroup(bloodGroup);
    viewModel.setComponentStatus(componentStatus);
    return viewModel;
  }
  
  public static InventoryViewModelBuilder anInventoryViewModel() {
    return new InventoryViewModelBuilder();
  }

}

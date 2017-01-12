package org.jembi.bsis.helpers.builders;

import java.util.Date;

import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.InventoryViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;

public class InventoryViewModelBuilder extends AbstractBuilder<InventoryViewModel> {

  private Long id;
  private ComponentTypeViewModel componentType;
  private Date createdOn;
  private String donationIdentificationNumber;
  private InventoryStatus inventoryStatus;
  private String expiryStatus;
  private String componentCode;
  private Date expiresOn;
  private LocationFullViewModel location;
  private String bloodGroup;

  public InventoryViewModelBuilder withId(Long id) {
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

  public InventoryViewModelBuilder withExpiryStatus(String expiryStatus) {
    this.expiryStatus = expiryStatus;
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

  public InventoryViewModelBuilder withLocation(LocationFullViewModel location) {
    this.location = location;
    return this;
  }

  public InventoryViewModelBuilder withBloodGroup(String bloodGroup) {
    this.bloodGroup = bloodGroup;
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
    viewModel.setExpiryStatus(expiryStatus);
    viewModel.setExpiresOn(expiresOn);
    viewModel.setLocation(location);
    viewModel.setBloodGroup(bloodGroup);
    return viewModel;
  }
  
  public static InventoryViewModelBuilder anInventoryViewModel() {
    return new InventoryViewModelBuilder();
  }

}

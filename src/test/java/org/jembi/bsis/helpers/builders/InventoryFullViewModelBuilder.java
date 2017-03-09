package org.jembi.bsis.helpers.builders;

import java.util.Date;

import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.InventoryFullViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.OrderFormFullViewModel;

public class InventoryFullViewModelBuilder extends AbstractBuilder<InventoryFullViewModel>{

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
  private OrderFormFullViewModel orderForm;

  public InventoryFullViewModelBuilder withOrderForm(OrderFormFullViewModel orderForm) {
    this.orderForm = orderForm;
    return this;
  }
  
  public InventoryFullViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }
  
  public InventoryFullViewModelBuilder withInventoryStatus(InventoryStatus inventoryStatus) {
    this.inventoryStatus = inventoryStatus;
    return this;
  }
  
  public InventoryFullViewModelBuilder withComponentType(ComponentTypeViewModel componentType) {
    this.componentType = componentType;
    return this;
  }
  
  public InventoryFullViewModelBuilder withDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }
  
  public InventoryFullViewModelBuilder withCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
    return this;
  }
  
  public InventoryFullViewModelBuilder withExpiryStatus(String expiryStatus) {
    this.expiryStatus = expiryStatus;
    return this;
  }
  
  public InventoryFullViewModelBuilder withComponentCode(String componentCode) {
    this.componentCode = componentCode;
    return this;
  }
  
  public InventoryFullViewModelBuilder withExpiresOn(Date expiresOn) {
    this.expiresOn = expiresOn;
    return this;
  }
  
  public InventoryFullViewModelBuilder withLocation(LocationFullViewModel location) {
    this.location = location;
    return this;
  }
  
  public InventoryFullViewModelBuilder withBloodGroup(String bloodGroup) {
    this.bloodGroup = bloodGroup;
    return this;
  }
  
  
  @Override
  public InventoryFullViewModel build() {
    InventoryFullViewModel viewModel = new InventoryFullViewModel();
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
    viewModel.setOrderform(orderForm);
    return viewModel;
  }
  
  public static InventoryFullViewModelBuilder anInventoryFullViewModel() {
    return new InventoryFullViewModelBuilder();
  }

  

}

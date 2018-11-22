package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.InventoryFullViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.OrderFormViewModel;

public class InventoryFullViewModelBuilder extends AbstractBuilder<InventoryFullViewModel>{

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
  private List<OrderFormViewModel> orderForms = new ArrayList<>();

  public InventoryFullViewModelBuilder withOrderForm(OrderFormViewModel orderForm) {
    this.orderForms.add(orderForm);
    return this;
  }

  public InventoryFullViewModelBuilder withOrderForms(List<OrderFormViewModel> orderForms) {
    this.orderForms = orderForms;
    return this;
  }
  
  public InventoryFullViewModelBuilder withId(UUID id) {
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
  
  public InventoryFullViewModelBuilder withDaysToExpire(int daysToExpire) {
    this.daysToExpire = daysToExpire;
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
  
  public InventoryFullViewModelBuilder withLocation(LocationViewModel location) {
    this.location = location;
    return this;
  }
  
  public InventoryFullViewModelBuilder withBloodGroup(String bloodGroup) {
    this.bloodGroup = bloodGroup;
    return this;
  }

  public InventoryFullViewModelBuilder withComponentStatus(ComponentStatus componentStatus) {
    this.componentStatus = componentStatus;
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
    viewModel.setDaysToExpire(daysToExpire);
    viewModel.setExpiresOn(expiresOn);
    viewModel.setLocation(location);
    viewModel.setBloodGroup(bloodGroup);
    viewModel.setComponentStatus(componentStatus);
    viewModel.setOrderForms(orderForms);
    return viewModel;
  }
  
  public static InventoryFullViewModelBuilder anInventoryFullViewModel() {
    return new InventoryFullViewModelBuilder();
  }
}

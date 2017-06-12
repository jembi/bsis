package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.viewmodel.ComponentManagementViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;

public class ComponentManagementViewModelBuilder extends AbstractBuilder<ComponentManagementViewModel> {

  private UUID id;
  private ComponentTypeViewModel componentType;
  private Date createdOn;
  private Date expiresOn;
  private ComponentStatus status;
  private int daysToExpire;
  private String componentCode;
  private Integer weight;
  private Map<String, Boolean> permissions = new HashMap<>();
  private boolean hasComponentBatch = false;
  private InventoryStatus inventoryStatus;
  private Date donationDateTime;
  private Date bleedStartTime;
  private Date bleedEndTime;
  private UUID parentComponentId;

  public ComponentManagementViewModelBuilder withDonationDateTime(Date donationDateTime) {
    this.donationDateTime = donationDateTime;
    return this;
  }

  public ComponentManagementViewModelBuilder withBleedStartTime(Date bleedStartTime) {
    this.bleedStartTime = bleedStartTime;
    return this;
  }

  public ComponentManagementViewModelBuilder withBleedEndTime(Date bleedEndTime) {
    this.bleedEndTime = bleedEndTime;
    return this;
  }

  public ComponentManagementViewModelBuilder whichHasComponentBatch() {
    this.hasComponentBatch = true;
    return this;
  }

  public ComponentManagementViewModelBuilder whichHasNoComponentBatch() {
    this.hasComponentBatch = false;
    return this;
  }

  public ComponentManagementViewModelBuilder withInventoryStatus(InventoryStatus inventoryStatus) {
    this.inventoryStatus = inventoryStatus;
    return this;
  }

  public ComponentManagementViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public ComponentManagementViewModelBuilder withComponentType(ComponentTypeViewModel componentType) {
    this.componentType = componentType;
    return this;
  }

  public ComponentManagementViewModelBuilder withCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
    return this;
  }

  public ComponentManagementViewModelBuilder withExpiresOn(Date expiresOn) {
    this.expiresOn = expiresOn;
    return this;
  }
  
  public ComponentManagementViewModelBuilder withStatus(ComponentStatus status) {
    this.status = status;
    return this;
  }

  public ComponentManagementViewModelBuilder withDaysToExpire(int daysToExpire) {
    this.daysToExpire = daysToExpire;
    return this;
  }

  public ComponentManagementViewModelBuilder withComponentCode(String componentCode) {
    this.componentCode = componentCode;
    return this;
  }

  public ComponentManagementViewModelBuilder withWeigth(Integer weigth) {
    this.weight = weigth;
    return this;
  }

  public ComponentManagementViewModelBuilder withPermission(String name, boolean value) {
    permissions.put(name, value);
    return this;
  }


  public ComponentManagementViewModelBuilder withParentComponentId(UUID parentComponentId) {
    this.parentComponentId = parentComponentId;
    return this;
  }

  @Override
  public ComponentManagementViewModel build() {
    ComponentManagementViewModel viewModel = new ComponentManagementViewModel();
    viewModel.setId(id);
    viewModel.setCreatedOn(createdOn);
    viewModel.setExpiresOn(expiresOn);
    viewModel.setDaysToExpire(daysToExpire);
    viewModel.setStatus(status);
    viewModel.setComponentCode(componentCode);
    viewModel.setWeight(weight);
    viewModel.setComponentType(componentType);
    viewModel.setPermissions(permissions);
    viewModel.setHasComponentBatch(hasComponentBatch);
    viewModel.setInventoryStatus(inventoryStatus);
    viewModel.setDonationDateTime(donationDateTime);
    viewModel.setBleedStartTime(bleedStartTime);
    viewModel.setBleedEndTime(bleedEndTime);
    viewModel.setParentComponentId(parentComponentId);
    return viewModel;
  }
  
  public static ComponentManagementViewModelBuilder aComponentManagementViewModel() {
    return new ComponentManagementViewModelBuilder();
  }

}

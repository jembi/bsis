package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ComponentManagementViewModel extends BaseViewModel<UUID> {

  private ComponentTypeViewModel componentType;
  private Date createdOn;
  private Date expiresOn;
  private ComponentStatus status;
  private String expiryStatus;
  private String componentCode;
  private Integer weight;
  private Map<String, Boolean> permissions = new HashMap<>();
  private PackTypeFullViewModel packType;
  private boolean hasComponentBatch;
  private InventoryStatus inventoryStatus;
  private Date donationDateTime;
  private Date bleedStartTime;
  private Date bleedEndTime;
  private UUID parentComponentId;

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getBleedStartTime() {
    return bleedStartTime;
  }

  public void setBleedStartTime(Date bleedStartTime) {
    this.bleedStartTime = bleedStartTime;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getBleedEndTime() {
    return bleedEndTime;
  }

  public void setBleedEndTime(Date bleedEndTime) {
    this.bleedEndTime = bleedEndTime;
  }

  public boolean getHasComponentBatch() {
    return hasComponentBatch;
  }

  public void setHasComponentBatch(boolean hasComponentBatch) {
    this.hasComponentBatch = hasComponentBatch;
  }

  public InventoryStatus getInventoryStatus() {
    return inventoryStatus;
  }

  public void setInventoryStatus(InventoryStatus inventoryStatus) {
    this.inventoryStatus = inventoryStatus;
  }

  public ComponentTypeViewModel getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentTypeViewModel componentType) {
    this.componentType = componentType;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getExpiresOn() {
    return expiresOn;
  }

  public void setExpiresOn(Date expiresOn) {
    this.expiresOn = expiresOn;
  }
  
  public ComponentStatus getStatus() {
    return status;
  }

  public void setStatus(ComponentStatus status) {
    this.status = status;
  }
  public String getExpiryStatus() {
    return expiryStatus;
  }

  public void setExpiryStatus(String expiryStatus) {
    this.expiryStatus = expiryStatus;
  }

  public String getComponentCode() {
    return componentCode;
  }

  public void setComponentCode(String componentCode) {
    this.componentCode = componentCode;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public Map<String, Boolean> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }

  public PackTypeFullViewModel getPackType() {
    return packType;
  }

  public void setPackType(PackTypeFullViewModel packType) {
    this.packType = packType;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getDonationDateTime() {
    return donationDateTime;
  }

  public void setDonationDateTime(Date donationDateTime) {
    this.donationDateTime = donationDateTime;
  }

  public UUID getParentComponentId() {
    return parentComponentId;
  }

  public void setParentComponentId(UUID parentComponentId) {
    this.parentComponentId = parentComponentId;
  }
}

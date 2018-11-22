package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class InventoryViewModel {

  private UUID id;

  private String donationIdentificationNumber;

  private String componentCode;

  private Date createdOn;

  private ComponentTypeViewModel componentType;

  private int daysToExpire;

  private LocationViewModel location;

  private InventoryStatus inventoryStatus;

  private String bloodGroup;

  private Date expiresOn;

  private ComponentStatus status;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public String getComponentCode() {
    return componentCode;
  }

  public void setComponentCode(String componentCode) {
    this.componentCode = componentCode;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getCreatedOn() {
    return createdOn;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public int getDaysToExpire() {
    return daysToExpire;
  }

  public void setDaysToExpire(int daysToExpire) {
    this.daysToExpire = daysToExpire;
  }

  public ComponentTypeViewModel getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentTypeViewModel componentType) {
    this.componentType = componentType;
  }

  public LocationViewModel getLocation() {
    return location;
  }

  public void setLocation(LocationViewModel location) {
    this.location = location;
  }

  public InventoryStatus getInventoryStatus() {
    return inventoryStatus;
  }

  public void setInventoryStatus(InventoryStatus inventoryStatus) {
    this.inventoryStatus = inventoryStatus;
  }

  public String getBloodGroup() {
    return bloodGroup;
  }

  public void setBloodGroup(String bloodGroup) {
    this.bloodGroup = bloodGroup;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getExpiresOn() {
    return expiresOn;
  }

  public void setExpiresOn(Date expiresOn) {
    this.expiresOn = expiresOn;
  }

  public ComponentStatus getComponentStatus() {
    return status;
  }

  public void setComponentStatus(ComponentStatus status) {
    this.status = status;
  }
}

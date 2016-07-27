package org.jembi.bsis.viewmodel;

import java.util.Date;

import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ComponentFullViewModel extends ComponentViewModel {

  private LocationFullViewModel location;
  private Date expiresOn;
  private String notes;
  private PackTypeFullViewModel packType;
  private Date createdDate;
  private Date issuedOn;
  private Date discardedOn;
  private String bloodAbo;
  private String bloodRh;
  private InventoryStatus inventoryStatus;

  public LocationFullViewModel getLocation() {
    return location;
  }

  public void setLocation(LocationFullViewModel location) {
    this.location = location;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getExpiresOn() {
    return expiresOn;
  }

  public void setExpiresOn(Date expiresOn) {
    this.expiresOn = expiresOn;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public PackTypeFullViewModel getPackType() {
    return packType;
  }

  public void setPackType(PackTypeFullViewModel packType) {
    this.packType = packType;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getIssuedOn() {
    return issuedOn;
  }

  public void setIssuedOn(Date issuedOn) {
    this.issuedOn = issuedOn;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getDiscardedOn() {
    return discardedOn;
  }

  public void setDiscardedOn(Date discardedOn) {
    this.discardedOn = discardedOn;
  }

  public String getBloodAbo() {
    return bloodAbo;
  }

  public void setBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
  }
  public String getBloodRh() {
    return bloodRh;
  }

  public void setBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
  }

  public InventoryStatus getInventoryStatus() {
    return inventoryStatus;
  }

  public void setInventoryStatus(InventoryStatus inventoryStatus) {
    this.inventoryStatus = inventoryStatus;
  }

}

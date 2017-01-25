package org.jembi.bsis.viewmodel;

import java.util.Date;

import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ComponentFullViewModel extends ComponentViewModel {

  private String notes;
  private PackTypeFullViewModel packType;
  private Date createdDate;
  private Date issuedOn;
  private Date discardedOn;
  private String bloodAbo;
  private String bloodRh;
  private InventoryStatus inventoryStatus;
  private boolean isInitialComponent;

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

  public boolean getIsInitialComponent() {   
    return this.isInitialComponent;
  }

  public void setIsInitialComponent(boolean isInitialComponent) {
    this.isInitialComponent = isInitialComponent;
  }
}

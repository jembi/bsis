package org.jembi.bsis.backingform;

import java.util.Date;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.packtype.PackType;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ComponentBackingForm {

  private Long id;

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }
  
  @JsonIgnore
  public LocationBackingForm getLocation() {
    // ignore
    return null;
  }
  
  @JsonIgnore
  public InventoryStatus getInventoryStatus() {
    // ignore
    return null;
  }

  @JsonIgnore
  public ComponentStatus getStatus() {
    // ignore
    return null;
  }

  @JsonIgnore
  public void setNotes(String notes) {
    // ignore
  }

  @JsonIgnore
  public void setComponentType(ComponentType componentType) {
    // ignore
  }

  @JsonIgnore
  public void setExpiresOn(Date expiresOn) {
    // ignore
  }

  @JsonIgnore
  public void setCreatedOn(Date createdOn) {
    // ignore
  }

  @JsonIgnore
  public void setCreatedDate(Date createdDate) {
    // ignore
  }

  @JsonIgnore
  public void setStatus(ComponentStatus status) {
    // ignore
  }

  @JsonIgnore
  public void setDiscardedOn(Date discardedOn) {
    // ignore
  }

  @JsonIgnore
  public void setIssuedOn(Date issuedOn) {
    // ignore
  }

  @JsonIgnore
  public void setComponentCode(String componentCode) {
    // ignore
  }

  @JsonIgnore
  public void setInventoryStatus(InventoryStatus inventoryStatus) {
    // ignore
  }

  @JsonIgnore
  public void setLocation(LocationBackingForm location) {
    // ignore
  }

  @JsonIgnore
  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    // Ignore
  }

  @JsonIgnore
  public void setPackType(PackType packType) {
    // Ignore
  }

  @JsonIgnore
  public void setExpiryStatus(String expiryStatus) {
    // Ignore
  }

  @JsonIgnore
  public void setBloodAbo(String bloodAbo) {
    // Ignore
  }

  @JsonIgnore
  public void setBloodRh(String bloodRh) {
    // Ignore
  }

}
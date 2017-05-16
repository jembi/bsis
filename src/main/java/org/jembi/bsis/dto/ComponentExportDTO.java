package org.jembi.bsis.dto;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;

public class ComponentExportDTO extends ModificationTrackerExportDTO {

  private UUID id; // Used only for removing duplicates
  private String donationIdentificationNumber;
  private String componentCode;
  private String parentComponentCode;
  private Date createdOn;
  private ComponentStatus status;
  private String location;
  private Date issuedOn;
  private InventoryStatus inventoryStatus;
  private Date discardedOn;
  private String discardReason;
  private Date expiresOn;
  private String notes;
  
  public ComponentExportDTO() {
    // Default constructor
  }

  public ComponentExportDTO(UUID id, String donationIdentificationNumber, String componentCode, Date createdDate,
      String createdBy, Date lastUpdated, String lastUpdatedBy, String parentComponentCode, Date createdOn,
      ComponentStatus status, String location, Date issuedOn, InventoryStatus inventoryStatus, Date discardedOn,
      String discardReason, Date expiresOn, String notes) {
    this.id = id;
    this.donationIdentificationNumber = donationIdentificationNumber;
    this.componentCode = componentCode;
    this.createdDate = createdDate;
    this.createdBy = createdBy;
    this.lastUpdated = lastUpdated;
    this.lastUpdatedBy = lastUpdatedBy;
    this.parentComponentCode = parentComponentCode;
    this.createdOn = createdOn;
    this.status = status;
    this.location = location;
    this.issuedOn = issuedOn;
    this.inventoryStatus = inventoryStatus;
    this.discardedOn = discardedOn;
    this.discardReason = discardReason;
    this.expiresOn = expiresOn;
    this.notes = notes;
  }
  
  public UUID getId() {
    return this.id;
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

  public String getParentComponentCode() {
    return parentComponentCode;
  }

  public void setParentComponentCode(String parentComponentCode) {
    this.parentComponentCode = parentComponentCode;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
  }

  public ComponentStatus getStatus() {
    return status;
  }

  public void setStatus(ComponentStatus status) {
    this.status = status;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Date getIssuedOn() {
    return issuedOn;
  }

  public void setIssuedOn(Date issuedOn) {
    this.issuedOn = issuedOn;
  }

  public InventoryStatus getInventoryStatus() {
    return inventoryStatus;
  }

  public void setInventoryStatus(InventoryStatus inventoryStatus) {
    this.inventoryStatus = inventoryStatus;
  }

  public Date getDiscardedOn() {
    return discardedOn;
  }

  public void setDiscardedOn(Date discardedOn) {
    this.discardedOn = discardedOn;
  }

  public String getDiscardReason() {
    return discardReason;
  }

  public void setDiscardReason(String discardReason) {
    this.discardReason = discardReason;
  }

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

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof ComponentExportDTO)) {
      return false;
    }
    
    if (getId() == null) {
      return super.equals(other);
    }
    return getId().equals(((ComponentExportDTO) other).getId());          
  }

}

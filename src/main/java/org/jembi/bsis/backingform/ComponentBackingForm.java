package org.jembi.bsis.backingform;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.packtype.PackType;
import org.jembi.bsis.viewmodel.OrderFormViewModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ComponentBackingForm {

  private UUID id;
  private Integer weight;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
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
  public void setDonationFlagCharacters(String donationFlagCharacters) {
    // Ignore
  }

  @JsonIgnore
  public void setPackType(PackType packType) {
    // Ignore
  }

  @JsonIgnore
  public void setDaysToExpire(Integer daysToExpire) {
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
  
  @JsonIgnore
  public void setPermissions(Map<String, Boolean> permissions) {
    // Ignore
  }
  
  @JsonIgnore
  public void setComponentTypeCombination(Long componentTypeCombinationId) {
    // Ignore
  }
  
  @JsonIgnore
  public void setComponentTypeCombination(ComponentTypeCombinationBackingForm componentTypeCombination) {
    // Ignore
  }

  @JsonIgnore
  public void setHasComponentBatch(boolean hasComponentBatch) {
    // Ignore
  }

  @JsonIgnore
  public void setProcessedOn(Date processedOn) {
    // Ignore
  }

  @JsonIgnore
  public void setIsInitialComponent(boolean isInitialComponent) {
    // Ignore
  }

  @JsonIgnore
  public void setDonationDateTime(Date donationDateTime) {
    // Ignore
  }

  @JsonIgnore
  public void setParentComponentId(Long parentComponentId) {
    // Ignore
  }

  @JsonIgnore
  public void setOrderForms(List<OrderFormViewModel> orderForms) {
    // Ignore
  }

  @JsonIgnore
  public void setBloodGroup(String bloodGroup) {
    // Ignore
  }

  @JsonIgnore
  public void setComponentStatus(ComponentStatus componentStatus) {
    // Ignore
  }
}

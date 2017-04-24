package org.jembi.bsis.backingform;

import java.util.Set;
import java.util.UUID;

import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;

public class ComponentTypeBackingForm {
  
  private UUID id;
  private String componentTypeName;
  private String componentTypeCode;
  private Integer expiresAfter;
  private Integer maxBleedTime;
  private Integer maxTimeSinceDonation;
  private ComponentTypeTimeUnits expiresAfterUnits = ComponentTypeTimeUnits.DAYS;
  private Boolean hasBloodGroup;
  private String description;
  private boolean isDeleted = false;
  private Integer lowStorageTemperature;
  private Integer highStorageTemperature;
  private Integer lowTransportTemperature;
  private Integer highTransportTemperature;
  private String preparationInfo;
  private String transportInfo;
  private String storageInfo;
  private boolean canBeIssued = true;
  private boolean containsPlasma = true;
  private Double gravity;
  private Set<ComponentTypeCombination> producedComponentTypeCombinations;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Returns the max bleed time in minutes
   * @return
   */
  public Integer getMaxBleedTime() {
    return maxBleedTime;
  }
  
  /**
   * Returns the max time since donation in hours
   * @return
   */
  public Integer getMaxTimeSinceDonation() {
    return maxTimeSinceDonation;
  }
  
  /**
   * Sets the max bleed time (in minutes)
   * @param maxBleedTime
   */
  public void setMaxBleedTime(Integer maxBleedTime) {
    this.maxBleedTime = maxBleedTime;
  }
  
  /**
   * Sets the  max time since donation (in hours)
   * @param maxTimeSinceDonation
   */
  public void setMaxTimeSinceDonation(Integer maxTimeSinceDonation) {
    this.maxTimeSinceDonation = maxTimeSinceDonation;
  }

  public void setProducedComponentTypeCombinations(Set<ComponentTypeCombination> producedComponentTypeCombinations) {
    this.producedComponentTypeCombinations = producedComponentTypeCombinations;
  }

  public Set<ComponentTypeCombination> getProducedComponentTypeCombinations() {
    return this.producedComponentTypeCombinations;
  }
  
  public String getComponentTypeName() {
    return componentTypeName;
  }

  public void setComponentTypeName(String componentTypeName) {
    this.componentTypeName = componentTypeName;
  }

  public String getComponentTypeCode() {
    return componentTypeCode;
  }

  public void setComponentTypeCode(String componentTypeCode) {
    this.componentTypeCode = componentTypeCode;
  }

  public Integer getExpiresAfter() {
    return expiresAfter;
  }

  public void setExpiresAfter(Integer expiresAfter) {
    this.expiresAfter = expiresAfter;
  }

  public ComponentTypeTimeUnits getExpiresAfterUnits() {
    return expiresAfterUnits;
  }

  public void setExpiresAfterUnits(ComponentTypeTimeUnits expiresAfterUnits) {
    this.expiresAfterUnits = expiresAfterUnits;
  }

  public Boolean getHasBloodGroup() {
    return hasBloodGroup;
  }

  public void setHasBloodGroup(Boolean hasBloodGroup) {
    this.hasBloodGroup = hasBloodGroup;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public Integer getLowStorageTemperature() {
    return lowStorageTemperature;
  }

  public void setLowStorageTemperature(Integer lowStorageTemperature) {
    this.lowStorageTemperature = lowStorageTemperature;
  }

  public Integer getHighStorageTemperature() {
    return highStorageTemperature;
  }

  public void setHighStorageTemperature(Integer highStorageTemperature) {
    this.highStorageTemperature = highStorageTemperature;
  }

  public Integer getLowTransportTemperature() {
    return lowTransportTemperature;
  }

  public void setLowTransportTemperature(Integer lowTransportTemperature) {
    this.lowTransportTemperature = lowTransportTemperature;
  }

  public Integer getHighTransportTemperature() {
    return highTransportTemperature;
  }

  public void setHighTransportTemperature(Integer highTransportTemperature) {
    this.highTransportTemperature = highTransportTemperature;
  }

  public String getPreparationInfo() {
    return preparationInfo;
  }

  public void setPreparationInfo(String preparationInfo) {
    this.preparationInfo = preparationInfo;
  }

  public String getTransportInfo() {
    return transportInfo;
  }

  public void setTransportInfo(String transportInfo) {
    this.transportInfo = transportInfo;
  }

  public String getStorageInfo() {
    return storageInfo;
  }

  public void setStorageInfo(String storageInfo) {
    this.storageInfo = storageInfo;
  }

  public boolean getCanBeIssued() {
    return canBeIssued;
  }

  public void setCanBeIssued(boolean canBeIssued) {
    this.canBeIssued = canBeIssued;
  }

  public boolean getContainsPlasma() {
    return containsPlasma;
  }

  public void setContainsPlasma(boolean containsPlasma) {
    this.containsPlasma = containsPlasma;
  }

  public Double getGravity() {
    return gravity;
  }

  public void setGravity(Double gravity) {
    this.gravity = gravity;
  }
}

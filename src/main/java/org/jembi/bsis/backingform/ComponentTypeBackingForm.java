package org.jembi.bsis.backingform;

import java.util.List;
import java.util.Set;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ComponentTypeBackingForm {

  @JsonIgnore
  private ComponentType componentType;

  public ComponentTypeBackingForm() {
    componentType = new ComponentType();
  }

  public ComponentType getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentType componentType) {
    this.componentType = componentType;
  }
  
  public Long getId() {
    return componentType.getId();
  }

  public void setId(Long id) {
    componentType.setId(id);
  }

  public String getComponentTypeName() {
    return componentType.getComponentTypeName();
  }
  
  public boolean getContainsPlasma() {
    return componentType.getContainsPlasma();
  }
  
  public void setComponentTypeName(String componentTypeName) {
    componentType.setComponentTypeName(componentTypeName);
  }

  public String getComponentTypeCode() {
    return componentType.getComponentTypeCode();
  }

  public void setComponentTypeCode(String componentTypeCode) {
    componentType.setComponentTypeCode(componentTypeCode);
  }
  
  public Integer getExpiresAfter() {
    return componentType.getExpiresAfter();
  }
  
  /**
   * Returns the max bleed time in minutes
   * @return
   */
  public Integer getMaxBleedTime() {
    return componentType.getMaxBleedTime();
  }
  
  /**
   * Returns the max time since donation in hours
   * @return
   */
  public Integer getMaxTimeSinceDonation() {
    return componentType.getMaxTimeSinceDonation();
  }
  
  /**
   * Sets the max bleed time (in minutes)
   * @param maxBleedTime
   */
  public void setMaxBleedTime(Integer maxBleedTime) {
    componentType.setMaxBleedTime(maxBleedTime);
  }
  
  /**
   * Sets the  max time since donation (in hours)
   * @param maxTimeSinceDonation
   */
  public void setMaxTimeSinceDonation(Integer maxTimeSinceDonation) {
    componentType.setMaxTimeSinceDonation(maxTimeSinceDonation);
  } 

  public void setExpiresAfter(Integer expiresAfter) {
    componentType.setExpiresAfter(expiresAfter);
  }

  public void setExpiresAfterUnits(String componentTypeTimeUnits) {
    componentType.setExpiresAfterUnits(ComponentTypeTimeUnits.valueOf(componentTypeTimeUnits));
  }

  public void setDescription(String description) {
    componentType.setDescription(description);
  }

  public void setHasBloodGroup(Boolean hasBloodGroup) {
    componentType.setHasBloodGroup(hasBloodGroup);
  }

  public void setComponentTypeCombinations(List<ComponentTypeCombination> componentTypeCombinations) {
    componentType.setComponentTypeCombinations(componentTypeCombinations);
  }

  public void setProducedComponentTypeCombinations(Set<ComponentTypeCombination> producedComponentTypeCombinations) {
    componentType.setProducedComponentTypeCombinations(producedComponentTypeCombinations);
  }

  public void setHighStorageTemperature(Integer highStorageTemperature) {
    componentType.setHighStorageTemperature(highStorageTemperature);
  }

  public void setLowStorageTemperature(Integer lowStorageTemperature) {
    componentType.setLowStorageTemperature(lowStorageTemperature);
  }

  public void setLowTransportTemperature(Integer lowTransportTemperature) {
    componentType.setLowTransportTemperature(lowTransportTemperature);
  }

  public void setHighTransportTemperature(Integer highTransportTemperature) {
    componentType.setHighTransportTemperature(highTransportTemperature);
  }

  public void setPreparationInfo(String preparationInfo) {
    componentType.setPreparationInfo(preparationInfo);
  }

  public void setTransportInfo(String transportInfo) {
    componentType.setTransportInfo(transportInfo);
  }

  public void setStorageInfo(String storageInfo) {
    componentType.setStorageInfo(storageInfo);
  }

  public void setCanBeIssued(boolean canBeIssued) {
    componentType.setCanBeIssued(canBeIssued);
  }

  public void setIsDeleted(boolean isDeleted) {
    componentType.setIsDeleted(isDeleted);
  }
  
  public void setContainsPlasma(boolean containsPlasma) {
    componentType.setContainsPlasma(containsPlasma);
  }
  
}

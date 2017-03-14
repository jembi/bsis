package org.jembi.bsis.viewmodel;

import java.util.List;

public class ComponentTypeFullViewModel extends ComponentTypeSearchViewModel {

  private Boolean hasBloodGroup;
  private Integer lowStorageTemperature;
  private Integer highStorageTemperature;
  private String preparationInfo;
  private String transportInfo;
  private String storageInfo;
  List<ComponentTypeCombinationViewModel> producedComponentTypeCombinations;
  private Integer maxBleedTime;
  private Integer maxTimeSinceDonation;
  private Double gravity;

  public Boolean isHasBloodGroup() {
    return hasBloodGroup;
  }

  public Integer getLowStorageTemperature() {
    return lowStorageTemperature;
  }

  public Integer getHighStorageTemperature() {
    return highStorageTemperature;
  }

  public String getPreparationInfo() {
    return preparationInfo;
  }

  public String getStorageInfo() {
    return storageInfo;
  }

  public List<ComponentTypeCombinationViewModel> getProducedComponentTypeCombinations() {
    return producedComponentTypeCombinations;
  }

  public void setHasBloodGroup(Boolean hasBloodGroup) {
    this.hasBloodGroup = hasBloodGroup;
  }

  public void setLowStorageTemperature(Integer lowStorageTemperature) {
    this.lowStorageTemperature = lowStorageTemperature;
  }

  public void setHighStorageTemperature(Integer highStorageTemperature) {
    this.highStorageTemperature = highStorageTemperature;
  }

  public void setPreparationInfo(String preparationInfo) {
    this.preparationInfo = preparationInfo;
  }

  public void setStorageInfo(String storageInfo) {
    this.storageInfo = storageInfo;
  }

  public void setProducedComponentTypeCombinations(
      List<ComponentTypeCombinationViewModel> producedComponentTypeCombinations) {
    this.producedComponentTypeCombinations = producedComponentTypeCombinations;
  }
  
  public String getTransportInfo() {
    return transportInfo;
  }

  public void setTransportInfo(String transportInfo) {
    this.transportInfo = transportInfo;
  }

  public Integer getMaxBleedTime() {
    return maxBleedTime;
  }

  public void setMaxBleedTime(Integer maxBleedTime) {
    this.maxBleedTime = maxBleedTime;
  }

  public Integer getMaxTimeSinceDonation() {
    return maxTimeSinceDonation;
  }

  public void setMaxTimeSinceDonation(Integer maxTimeSinceDonation) {
    this.maxTimeSinceDonation = maxTimeSinceDonation;
  }

  public Double getGravity() {
    return gravity;
  }

  public void setGravity(Double gravity) {
    this.gravity = gravity;
  }
  
}

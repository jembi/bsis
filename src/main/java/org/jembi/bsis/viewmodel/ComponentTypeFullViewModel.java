package org.jembi.bsis.viewmodel;

import java.util.List;

public class ComponentTypeFullViewModel extends ComponentTypeSearchViewModel {

  private boolean hasBloodGroup;
  private Integer lowStorageTemperature;
  private Integer highStorageTemperature;
  private String preparationInfo;
  private String transportInfo;
  private String storageInfo;
  List<ComponentTypeCombinationViewModel> producedComponentTypeCombinations;

  public boolean isHasBloodGroup() {
    return hasBloodGroup;
  }

  public int getLowStorageTemperature() {
    return lowStorageTemperature;
  }

  public int getHighStorageTemperature() {
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

  public void setHasBloodGroup(boolean hasBloodGroup) {
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

  public void getProducedComponentTypeCombinations(
      List<ComponentTypeCombinationViewModel> producedComponentTypeCombinations) {
    this.producedComponentTypeCombinations = producedComponentTypeCombinations;
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
}

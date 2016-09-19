package org.jembi.bsis.viewmodel;

import java.util.List;

public class ComponentTypeFullViewModel extends ComponentTypeSearchViewModel {

  private boolean hasBloodGroup;
  private Integer lowStorageTemperature;
  private Integer highStorageTemperature;
  private String preparationInfo;
  private String storageInfo;
  List<ComponentTypeCombinationViewModel> producedComponentTypeCombinations;

  public boolean isHasBloodGroup() {
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

  
  // should use factory
  /*
   * private List<ComponentTypeCombinationViewModel>
   * getComponentTypeCombinationViewModels(List<ComponentTypeCombination> componentTypeCombinations)
   * {
   * 
   * List<ComponentTypeCombinationViewModel> componentTypeCombinationViewModels = new
   * ArrayList<ComponentTypeCombinationViewModel>(); if (componentTypeCombinations != null) { for
   * (ComponentTypeCombination componentTypeCombination : componentTypeCombinations) {
   * componentTypeCombinationViewModels.add(new
   * ComponentTypeCombinationViewModel(componentTypeCombination)); } }
   * 
   * return componentTypeCombinationViewModels; }
   */
}

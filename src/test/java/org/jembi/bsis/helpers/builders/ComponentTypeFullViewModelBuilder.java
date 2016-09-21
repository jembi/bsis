package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeFullViewModel;

public class ComponentTypeFullViewModelBuilder extends AbstractBuilder<ComponentTypeFullViewModel> {

  private Long Id;
  private String componentTypeName;
  private String componentTypeCode;
  private String description;
  private boolean hasBloodGroup;
  private Integer lowStorageTemperature;
  private Integer highStorageTemperature;
  private String preparationInfo;
  private String transportInfo;
  private String storageInfo;
  private int expiresAfter;
  private boolean canBeIssued;
  private boolean isDeleted;
  private ComponentTypeTimeUnits expiresAfterUnits = ComponentTypeTimeUnits.DAYS;
  private List<ComponentTypeCombinationViewModel> producedComponentTypeCombinations = new ArrayList<>();
  private boolean containsPlasma;

  public ComponentTypeFullViewModelBuilder withId(Long Id) {
    this.Id = Id;
    return this;
  }

  public ComponentTypeFullViewModelBuilder withComponentTypeName(String componentTypeName) {
    this.componentTypeName = componentTypeName;
    return this;
  }

  public ComponentTypeFullViewModelBuilder withComponentTypeCode(String componentTypeCode) {
    this.componentTypeCode = componentTypeCode;
    return this;
  }

  public ComponentTypeFullViewModelBuilder withDescription(String description) {
    this.description = description;
    return this;
  }
  
  public ComponentTypeFullViewModelBuilder withHasBloodGroup(boolean hasBloodGroup) {
    this.hasBloodGroup = hasBloodGroup;
    return this;
  }

  public ComponentTypeFullViewModelBuilder withLowStorageTemperature(Integer lowStorageTemperature) {
    this.lowStorageTemperature = lowStorageTemperature;
    return this;
  }

  public ComponentTypeFullViewModelBuilder withHighStorageTemperature(Integer highStorageTemperature) {
    this.highStorageTemperature = highStorageTemperature;
    return this;
  }

  public ComponentTypeFullViewModelBuilder withPreparationInfo(String preparationInfo) {
    this.preparationInfo = preparationInfo;
    return this;
  }

  public ComponentTypeFullViewModelBuilder withStorageInfo(String storageInfo) {
    this.storageInfo = storageInfo;
    return this;
  }

  public ComponentTypeFullViewModelBuilder withTransportInfo(String transportInfo) {
    this.transportInfo = transportInfo;
    return this;
  }
  
  public ComponentTypeFullViewModelBuilder withExpiresAfter(int expiresAfter) {
    this.expiresAfter = expiresAfter;
    return this;
  }

  public ComponentTypeFullViewModelBuilder withIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }

  public ComponentTypeFullViewModelBuilder withCanBeIssued(boolean canBeIssued) {
    this.canBeIssued = canBeIssued;
    return this;
  }

  public ComponentTypeFullViewModelBuilder withExpiresAfterUnits(ComponentTypeTimeUnits expiresAfterUnits) {
    this.expiresAfterUnits = expiresAfterUnits;
    return this;
  }

  public ComponentTypeFullViewModelBuilder withProducedComponentTypeCombination(
      ComponentTypeCombinationViewModel producedComponentTypeCombination) {
    this.producedComponentTypeCombinations.add(producedComponentTypeCombination);
    return this;
  }

  public ComponentTypeFullViewModelBuilder withProducedComponentTypeCombinations(
      List<ComponentTypeCombinationViewModel> producedComponentTypeCombinations) {
    this.producedComponentTypeCombinations = producedComponentTypeCombinations;
    return this;
  }
  
  public ComponentTypeFullViewModelBuilder withContainsPlasma(boolean containsPlasma) {
    this.containsPlasma = containsPlasma;
    return this;
  }
  
  @Override
  public ComponentTypeFullViewModel build() {
    ComponentTypeFullViewModel viewModel = new ComponentTypeFullViewModel();
    viewModel.setId(Id);
    viewModel.setHasBloodGroup(hasBloodGroup);
    viewModel.setLowStorageTemperature(lowStorageTemperature);
    viewModel.setHighStorageTemperature(highStorageTemperature);
    viewModel.setPreparationInfo(preparationInfo);
    viewModel.setStorageInfo(storageInfo);
    viewModel.setProducedComponentTypeCombinations(producedComponentTypeCombinations);
    viewModel.setExpiresAfter(expiresAfter);
    viewModel.setTransportInfo(transportInfo);
    viewModel.setCanBeIssued(canBeIssued);
    viewModel.setIsDeleted(isDeleted);
    viewModel.setExpiresAfterUnits(expiresAfterUnits);
    viewModel.setComponentTypeName(componentTypeName);
    viewModel.setComponentTypeCode(componentTypeCode);
    viewModel.setDescription(description);
    viewModel.setIsContainsPlasma(containsPlasma);

    return viewModel;
  }

  public static ComponentTypeFullViewModelBuilder aComponentTypeFullViewModel() {
    return new ComponentTypeFullViewModelBuilder();
  }

}

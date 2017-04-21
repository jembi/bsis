package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;
import org.jembi.bsis.viewmodel.ComponentTypeCombinationViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeFullViewModel;

public class ComponentTypeFullViewModelBuilder extends AbstractBuilder<ComponentTypeFullViewModel> {

  private UUID Id;
  private String componentTypeName;
  private String componentTypeCode;
  private String description;
  private Boolean hasBloodGroup;
  private Integer lowStorageTemperature;
  private Integer highStorageTemperature;
  private String preparationInfo;
  private String transportInfo;
  private String storageInfo;
  private Integer expiresAfter;
  private boolean canBeIssued;
  private boolean isDeleted = false;
  private ComponentTypeTimeUnits expiresAfterUnits = ComponentTypeTimeUnits.DAYS;
  private List<ComponentTypeCombinationViewModel> producedComponentTypeCombinations = new ArrayList<>();
  private boolean containsPlasma = false;

  public ComponentTypeFullViewModelBuilder withId(UUID Id) {
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
  
  public ComponentTypeFullViewModelBuilder withHasBloodGroup(Boolean hasBloodGroup) {
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
  
  public ComponentTypeFullViewModelBuilder withExpiresAfter(Integer expiresAfter) {
    this.expiresAfter = expiresAfter;
    return this;
  }

  public ComponentTypeFullViewModelBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  public ComponentTypeFullViewModelBuilder thatCanBeIssued() {
    this.canBeIssued = true;
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
  
  public ComponentTypeFullViewModelBuilder thatContainsPlasma(boolean containsPlasma) {
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
    viewModel.setContainsPlasma(containsPlasma);
    return viewModel;
  }

  public static ComponentTypeFullViewModelBuilder aComponentTypeFullViewModel() {
    return new ComponentTypeFullViewModelBuilder();
  }

}

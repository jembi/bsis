package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;
import org.jembi.bsis.viewmodel.ComponentTypeSearchViewModel;

public class ComponentTypeSearchViewModelBuilder extends AbstractBuilder<ComponentTypeSearchViewModel> {

  private Long id;
  private int expiresAfter;
  private boolean canBeIssued;
  private boolean isDeleted;
  private ComponentTypeTimeUnits expiresAfterUnits = ComponentTypeTimeUnits.DAYS;
  private boolean containsPlasma;  
  private String componentTypeName;
  private String componentTypeCode;
  private String description;
  
  public ComponentTypeSearchViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }
  
  public ComponentTypeSearchViewModelBuilder withExpiresAfter(int expiresAfter) {
    this.expiresAfter = expiresAfter;
    return this;
  }

  public ComponentTypeSearchViewModelBuilder withCanBeIssued(boolean canBeIssued) {
    this.canBeIssued = canBeIssued;
    return this;
  }

  public ComponentTypeSearchViewModelBuilder withIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }
  
  public ComponentTypeSearchViewModelBuilder withExpiresAfterUnits(ComponentTypeTimeUnits expiresAfterUnits) {
    this.expiresAfterUnits = expiresAfterUnits;
    return this;
  }

  public ComponentTypeSearchViewModelBuilder withContainsPlasma(boolean containsPlasma) {
    this.containsPlasma = containsPlasma;
    return this;
  }
  
  public ComponentTypeSearchViewModelBuilder withComponentTypeName(String componentTypeName) {
    this.componentTypeName = componentTypeName;
    return this;
  }

  public ComponentTypeSearchViewModelBuilder withComponentTypeCode(String componentTypeCode) {
    this.componentTypeCode = componentTypeCode;
    return this;
  }

  public ComponentTypeSearchViewModelBuilder withDescription(String description) {
    this.description = description;
    return this;
  }
  
  @Override
  public ComponentTypeSearchViewModel build() {
    ComponentTypeSearchViewModel viewModel = new ComponentTypeSearchViewModel();
    viewModel.setId(id);
    viewModel.setExpiresAfter(expiresAfter);
    viewModel.setCanBeIssued(canBeIssued);
    viewModel.setIsDeleted(isDeleted);
    viewModel.setExpiresAfterUnits(expiresAfterUnits);
    viewModel.setIsContainsPlasma(containsPlasma);
    viewModel.setComponentTypeName(componentTypeName);
    viewModel.setComponentTypeCode(componentTypeCode);
    viewModel.setDescription(description);
    
    return viewModel;
  }
  
  public static ComponentTypeSearchViewModelBuilder aComponentTypeSearchViewModel() {
    return new ComponentTypeSearchViewModelBuilder();
  }

}

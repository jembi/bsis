package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.model.componenttype.ComponentTypeTimeUnits;
import org.jembi.bsis.viewmodel.ComponentTypeSearchViewModel;

public class ComponentTypeSearchViewModelBuilder extends AbstractBuilder<ComponentTypeSearchViewModel> {

  private UUID id;
  private int expiresAfter;
  private boolean canBeIssued = false;
  private boolean isDeleted = false;
  private ComponentTypeTimeUnits expiresAfterUnits = ComponentTypeTimeUnits.DAYS;
  private boolean containsPlasma = false;  
  private String componentTypeName;
  private String componentTypeCode;
  private String description;
  
  public ComponentTypeSearchViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }
  
  public ComponentTypeSearchViewModelBuilder withExpiresAfter(int expiresAfter) {
    this.expiresAfter = expiresAfter;
    return this;
  }

  public ComponentTypeSearchViewModelBuilder thatCanBeIssued() {
    this.canBeIssued = true;
    return this;
  }

  public ComponentTypeSearchViewModelBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }
  
  public ComponentTypeSearchViewModelBuilder withExpiresAfterUnits(ComponentTypeTimeUnits expiresAfterUnits) {
    this.expiresAfterUnits = expiresAfterUnits;
    return this;
  }

  public ComponentTypeSearchViewModelBuilder thatContainsPlasma() {
    this.containsPlasma = true;
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
    viewModel.setContainsPlasma(containsPlasma);
    viewModel.setComponentTypeName(componentTypeName);
    viewModel.setComponentTypeCode(componentTypeCode);
    viewModel.setDescription(description);
    return viewModel;
  }
  
  public static ComponentTypeSearchViewModelBuilder aComponentTypeSearchViewModel() {
    return new ComponentTypeSearchViewModelBuilder();
  }

}

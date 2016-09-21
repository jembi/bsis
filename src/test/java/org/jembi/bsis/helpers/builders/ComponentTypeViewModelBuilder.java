package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.viewmodel.ComponentTypeViewModel;

public class ComponentTypeViewModelBuilder extends AbstractBuilder<ComponentTypeViewModel> {

  private Long id;
  private String componentTypeName;
  private String componentTypeCode;
  private String description;
  private boolean containsPlasma;

  public ComponentTypeViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ComponentTypeViewModelBuilder withComponentTypeName(String componentTypeName) {
    this.componentTypeName = componentTypeName;
    return this;
  }

  public ComponentTypeViewModelBuilder withComponentTypeCode(String componentTypeCode) {
    this.componentTypeCode = componentTypeCode;
    return this;
  }

  public ComponentTypeViewModelBuilder withDescription(String description) {
    this.description = description;
    return this;
  }
  
  public ComponentTypeViewModelBuilder withContainsPlasma(boolean containsPlasma) {
    this.containsPlasma = containsPlasma;
    return this;
  }

  @Override
  public ComponentTypeViewModel build() {
    ComponentTypeViewModel viewModel = new ComponentTypeViewModel();
    viewModel.setId(id);
    viewModel.setComponentTypeName(componentTypeName);
    viewModel.setComponentTypeCode(componentTypeCode);
    viewModel.setDescription(description);
    viewModel.setIsContainsPlasma(containsPlasma);

    return viewModel;
  }

  public static ComponentTypeViewModelBuilder aComponentTypeViewModelBuilder() {
    return new ComponentTypeViewModelBuilder();
  }
}

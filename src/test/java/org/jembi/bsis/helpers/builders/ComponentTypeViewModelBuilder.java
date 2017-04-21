package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.viewmodel.ComponentTypeViewModel;

public class ComponentTypeViewModelBuilder extends AbstractBuilder<ComponentTypeViewModel> {

  private UUID id;
  private String componentTypeName;
  private String componentTypeCode;
  private String description;
  private Integer maxBleedTime;
  private Integer maxTimeSinceDonation;

  public ComponentTypeViewModelBuilder withId(UUID id) {
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
  
  public ComponentTypeViewModelBuilder withMaxBleedTime(Integer maxBleedTime) {
    this.maxBleedTime = maxBleedTime;
    return this;
  }

  public ComponentTypeViewModelBuilder withMaxTimeSinceDonation(Integer maxTimeSinceDonation) {
    this.maxTimeSinceDonation = maxTimeSinceDonation;
    return this;
  }

  @Override
  public ComponentTypeViewModel build() {
    ComponentTypeViewModel viewModel = new ComponentTypeViewModel();
    viewModel.setId(id);
    viewModel.setComponentTypeName(componentTypeName);
    viewModel.setComponentTypeCode(componentTypeCode);
    viewModel.setDescription(description);
    viewModel.setMaxBleedTime(maxBleedTime);
    viewModel.setMaxTimeSinceDonation(maxTimeSinceDonation);

    return viewModel;
  }

  public static ComponentTypeViewModelBuilder aComponentTypeViewModel() {
    return new ComponentTypeViewModelBuilder();
  }
}

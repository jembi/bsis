package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.viewmodel.LocationManagementViewModel;

public class LocationManagementViewModelBuilder extends AbstractBuilder<LocationManagementViewModel> {

  private Long id;
  private String divisionLevel3Name;
  private boolean isDeleted;
  private String name;

  public LocationManagementViewModelBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public LocationManagementViewModelBuilder withDivisionLevel3Name(String divisionLevel3Name) {
    this.divisionLevel3Name = divisionLevel3Name;
    return this;
  }

  public LocationManagementViewModelBuilder isDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }

  public LocationManagementViewModelBuilder withName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public LocationManagementViewModel build() {
    LocationManagementViewModel viewModel = new LocationManagementViewModel();
    viewModel.setId(id);
    viewModel.setIsDeleted(isDeleted);
    viewModel.setName(name);
    viewModel.setDivisionLevel3(divisionLevel3Name);

    return viewModel;
  }

  public static LocationManagementViewModelBuilder aLocationManagementViewModelBuilder() {
    return new LocationManagementViewModelBuilder();
  }

}

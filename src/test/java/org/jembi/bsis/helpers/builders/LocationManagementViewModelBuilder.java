package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.viewmodel.DivisionViewModel;
import org.jembi.bsis.viewmodel.LocationManagementViewModel;

public class LocationManagementViewModelBuilder extends AbstractBuilder<LocationManagementViewModel> {

  private UUID id;
  private DivisionViewModel divisionLevel3;
  private boolean isDeleted;
  private String name;

  public LocationManagementViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public LocationManagementViewModelBuilder withDivisionLevel3(DivisionViewModel divisionLevel32) {
    this.divisionLevel3 = divisionLevel32;
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
    viewModel.setDivisionLevel3(divisionLevel3);

    return viewModel;
  }

  public static LocationManagementViewModelBuilder aLocationManagementViewModel() {
    return new LocationManagementViewModelBuilder();
  }

}

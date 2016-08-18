package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.viewmodel.LocationDivisionViewModel;

public class LocationDivisionViewModelBuilder extends AbstractBuilder<LocationDivisionViewModel> {
  
  private long id;
  private String name;
  private int level;
  
  public LocationDivisionViewModelBuilder withId(long id) {
    this.id = id;
    return this;
  }

  public LocationDivisionViewModelBuilder withName(String name) {
    this.name = name;
    return this;
  }

  public LocationDivisionViewModelBuilder withLevel(int level) {
    this.level = level;
    return this;
  }

  @Override
  public LocationDivisionViewModel build() {
    LocationDivisionViewModel viewModel = new LocationDivisionViewModel();
    viewModel.setId(id);
    viewModel.setName(name);
    viewModel.setLevel(level);
    return viewModel;
  }
  
  public static LocationDivisionViewModelBuilder aLocationDivisionViewModel() {
    return new LocationDivisionViewModelBuilder();
  }

}

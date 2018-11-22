package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.viewmodel.LocationViewModel;

public class LocationViewModelBuilder extends AbstractBuilder<LocationViewModel> {

  private UUID id;
  private String name;
  private boolean deleted;

  public LocationViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public LocationViewModelBuilder thatIsDeleted() {
    deleted = true;
    return this;
  }

  public LocationViewModelBuilder withName(String name) {
    this.name = name;
    return this;
  }

  @Override
  public LocationViewModel build() {
    LocationViewModel location = new LocationViewModel();
    location.setId(id);
    location.setName(name);
    location.setIsDeleted(deleted);
    return location;
  }

  public static LocationViewModelBuilder aLocationViewModel() {
    return new LocationViewModelBuilder();
  }

}

package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.springframework.stereotype.Service;

@Service
public class LocationViewModelFactory {
  
  public LocationViewModel createLocationViewModel(Location location) {
    return new LocationViewModel(location);
  }

  public List<LocationViewModel> createLocationViewModels(List<Location> locations) {
    List<LocationViewModel> viewModels = new ArrayList<>();
    if (locations != null) {
      for (Location location : locations) {
        viewModels.add(new LocationViewModel(location));
      }
    }
    return viewModels;
  }

}

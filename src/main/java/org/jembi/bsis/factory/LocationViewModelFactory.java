package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.springframework.stereotype.Service;

@Service
public class LocationViewModelFactory {
  
  public LocationFullViewModel createFullViewModel(Location location) {
    return new LocationFullViewModel(location);
  }

  public List<LocationFullViewModel> createFullViewModels(List<Location> locations) {
    List<LocationFullViewModel> viewModels = new ArrayList<>();
    if (locations != null) {
      for (Location location : locations) {
        viewModels.add(new LocationFullViewModel(location));
      }
    }
    return viewModels;
  }

}

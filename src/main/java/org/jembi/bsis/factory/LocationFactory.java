package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.springframework.stereotype.Service;

@Service
public class LocationFactory {
  
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

  public LocationViewModel createViewModel(Location location) {
    LocationViewModel viewModel = new LocationViewModel();
    viewModel.setId(location.getId());
    viewModel.setName(location.getName());
    viewModel.setIsDeleted(location.getIsDeleted());
    return viewModel;
  }

  public List<LocationViewModel> createViewModels(List<Location> locations) {
    List<LocationViewModel> viewModels = new ArrayList<>();
    if (locations != null) {
      for (Location location : locations) {
        viewModels.add(createViewModel(location));
      }
    }
    return viewModels;
  }

}

package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationFactory {
  
  @Autowired
  private DivisionFactory divisionFactory;

  public LocationFullViewModel createFullViewModel(Location location) {
    LocationFullViewModel viewModel = new LocationFullViewModel(location);
    if (location.getDivisionLevel1() != null) {
      viewModel.setDivisionLevel1(divisionFactory.createDivisionViewModel(location.getDivisionLevel1()));
    }
    if (location.getDivisionLevel2() != null) {
      viewModel.setDivisionLevel2(divisionFactory.createDivisionViewModel(location.getDivisionLevel2()));
    }
    if (location.getDivisionLevel3() != null) {
      viewModel.setDivisionLevel3(divisionFactory.createDivisionViewModel(location.getDivisionLevel3()));
    }
    return viewModel;
  }

  public List<LocationFullViewModel> createFullViewModels(List<Location> locations) {
    List<LocationFullViewModel> viewModels = new ArrayList<>();
    if (locations != null) {
      for (Location location : locations) {
        viewModels.add(createFullViewModel(location));
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

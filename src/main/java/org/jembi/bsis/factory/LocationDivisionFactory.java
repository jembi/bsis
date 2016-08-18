package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.location.LocationDivision;
import org.jembi.bsis.viewmodel.LocationDivisionViewModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class LocationDivisionFactory {
  
  public LocationDivisionViewModel createLocationDivisionViewModel(LocationDivision locationDivision) {
    LocationDivisionViewModel viewModel = new LocationDivisionViewModel();
    viewModel.setId(locationDivision.getId());
    viewModel.setName(locationDivision.getName());
    viewModel.setLevel(locationDivision.getLevel());
    return viewModel;
  }
  
  public List<LocationDivisionViewModel> createLocationDivisionViewModels(List<LocationDivision> locationDivisions) {
    List<LocationDivisionViewModel> viewModels = new ArrayList<>();
    for (LocationDivision locationDivision : locationDivisions) {
      viewModels.add(createLocationDivisionViewModel(locationDivision));
    }
    return viewModels;
  }

}

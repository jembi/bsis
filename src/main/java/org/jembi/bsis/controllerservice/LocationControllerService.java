package org.jembi.bsis.controllerservice;

import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.factory.DivisionFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.location.LocationType;
import org.jembi.bsis.repository.DivisionRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.viewmodel.DivisionViewModel;
import org.jembi.bsis.viewmodel.LocationFullViewModel;
import org.jembi.bsis.viewmodel.LocationManagementViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LocationControllerService {
  
  @Autowired
  private LocationRepository locationRepository;
  
  @Autowired
  private LocationFactory locationFactory;
  
  @Autowired
  private DivisionRepository divisionRepository;

  @Autowired
  private DivisionFactory divisionFactory;

  public LocationFullViewModel addLocation(LocationBackingForm form) {
    Location location = locationFactory.createEntity(form);
    locationRepository.saveLocation(location);
    return locationFactory.createFullViewModel(location);
  }

  public LocationFullViewModel updateLocation(LocationBackingForm form) {
    Location location = locationFactory.createEntity(form);
    Location updatedLocation = locationRepository.updateLocation(location);
    return locationFactory.createFullViewModel(updatedLocation);
  }

  public LocationFullViewModel getLocationById(UUID id) {
    Location location = locationRepository.getLocation(id);
    return locationFactory.createFullViewModel(location);
  }

  public void deleteLocation(UUID id) {
    locationRepository.deleteLocation(id);
  }

  public List<LocationManagementViewModel> findLocations(String name, boolean includeSimilarResults, LocationType locationType) {
    List<Location> locations = locationRepository.findLocations(name, includeSimilarResults, locationType, true);
    return locationFactory.createManagementViewModels(locations);
  }

  public List<DivisionViewModel> getLevel1Divisions() {
    return divisionFactory.createDivisionViewModels(divisionRepository.findDivisions("", false, 1, null), false);
  }

}

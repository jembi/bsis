package org.jembi.bsis.controllerservice;

import java.util.Collections;
import java.util.List;

import org.jembi.bsis.factory.LocationDivisionFactory;
import org.jembi.bsis.model.location.LocationDivision;
import org.jembi.bsis.viewmodel.LocationDivisionViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class LocationDivisionControllerService {
  
  @Autowired
  private LocationDivisionFactory locationDivisionFactory;
  
  public List<LocationDivisionViewModel> findLocationDivisions(String name, boolean includeSimilarResults,
      Integer level) {
    // TODO: Use query to find location divisions
    List<LocationDivision> locationDivisions = Collections.<LocationDivision>emptyList();
    return locationDivisionFactory.createLocationDivisionViewModels(locationDivisions);
  }

}

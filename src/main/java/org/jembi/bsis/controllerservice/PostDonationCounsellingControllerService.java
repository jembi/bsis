package org.jembi.bsis.controllerservice;

import java.util.List;

import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PostDonationCounsellingControllerService {
  
  @Autowired
  private LocationRepository locationRepository;
  @Autowired
  private LocationFactory locationFactory;
  
  public List<LocationViewModel> getVenues() {
    List<Location> venues = locationRepository.getVenues();
    return locationFactory.createViewModels(venues);
  }

}

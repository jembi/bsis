package org.jembi.bsis.controllerservice;

import java.util.List;

import javax.transaction.Transactional;

import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ReportsControllerService {

  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  @Autowired
  private ComponentTypeFactory componentTypeFactory;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private LocationFactory locationFactory;

  public List<ComponentTypeViewModel> getAllComponentTypesThatCanBeIssued() {
    return componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypesThatCanBeIssued());
  }
  
  public List<LocationViewModel> getDistributionSites() {
    return locationFactory.createViewModels(locationRepository.getDistributionSites());
  }

}

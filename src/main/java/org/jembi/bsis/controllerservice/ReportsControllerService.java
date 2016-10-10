package org.jembi.bsis.controllerservice;

import java.util.List;

import javax.transaction.Transactional;

import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.factory.DeferralReasonFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.repository.AdverseEventTypeRepository;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.repository.DeferralReasonRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.viewmodel.AdverseEventTypeViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.DeferralReasonViewModel;
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
  
  @Autowired
  private DeferralReasonRepository deferralReasonRepository;
  
  @Autowired
  private DeferralReasonFactory deferralReasonFactory;

  @Autowired
  private AdverseEventTypeRepository adverseEventTypeRepository;

  public List<ComponentTypeViewModel> getAllComponentTypesThatCanBeIssued() {
    return componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypesThatCanBeIssued());
  }
  
  public List<LocationViewModel> getDistributionSites() {
    return locationFactory.createViewModels(locationRepository.getDistributionSites());
  }
  
  public List<DeferralReasonViewModel> getDeferralReasons() {
    return deferralReasonFactory.createViewModels(deferralReasonRepository.getAllDeferralReasons());
  }

  public List<LocationViewModel> getVenues() {
    return locationFactory.createViewModels(locationRepository.getVenues());
  }

  public List<AdverseEventTypeViewModel> getAdverseEventTypes() {
    // FIXME: the ViewModel shouldn't be returned from the Repository (although it probably is quite efficient)
    return adverseEventTypeRepository.findNonDeletedAdverseEventTypeViewModels();
  }
}

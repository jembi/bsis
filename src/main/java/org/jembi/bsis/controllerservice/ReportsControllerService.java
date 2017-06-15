package org.jembi.bsis.controllerservice;

import java.util.List;

import javax.transaction.Transactional;

import org.jembi.bsis.factory.BloodTestFactory;
import org.jembi.bsis.factory.ComponentStatusChangeReasonFactory;
import org.jembi.bsis.factory.ComponentTypeFactory;
import org.jembi.bsis.factory.DeferralReasonFactory;
import org.jembi.bsis.factory.DonationTypeFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.factory.TransfusionReactionTypeFactory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.repository.AdverseEventTypeRepository;
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.repository.ComponentTypeRepository;
import org.jembi.bsis.repository.DeferralReasonRepository;
import org.jembi.bsis.repository.DiscardReasonRepository;
import org.jembi.bsis.repository.DonationTypeRepository;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.TransfusionReactionTypeRepository;
import org.jembi.bsis.viewmodel.AdverseEventTypeViewModel;
import org.jembi.bsis.viewmodel.BloodTestViewModel;
import org.jembi.bsis.viewmodel.ComponentTypeViewModel;
import org.jembi.bsis.viewmodel.DeferralReasonViewModel;
import org.jembi.bsis.viewmodel.DiscardReasonViewModel;
import org.jembi.bsis.viewmodel.DonationTypeViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.TransfusionReactionTypeViewModel;
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
  private DiscardReasonRepository discardReasonRepository;
  
  @Autowired
  private ComponentStatusChangeReasonFactory componentStatusChangeReasonFactory;

  @Autowired
  private AdverseEventTypeRepository adverseEventTypeRepository;

  @Autowired
  private TransfusionReactionTypeFactory transfusionReactionTypeFactory;

  @Autowired
  private TransfusionReactionTypeRepository transfusionReactionTypeRepository;

  @Autowired
  private BloodTestFactory bloodTestFactory;
  
  @Autowired
  private BloodTestRepository bloodTestRepository;

  @Autowired
  private DonationTypeRepository donationTypeRepository;

  @Autowired
  private DonationTypeFactory donationTypeFactory;

  public List<ComponentTypeViewModel> getAllComponentTypesThatCanBeIssued() {
    return componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypesThatCanBeIssued());
  }

  public List<ComponentTypeViewModel> getAllComponentTypes() {
    return componentTypeFactory.createViewModels(componentTypeRepository.getAllComponentTypes());
  }
  
  public List<DiscardReasonViewModel> getAllDiscardReasons(Boolean includeDeleted) {
    return componentStatusChangeReasonFactory.createDiscardReasonViewModels(discardReasonRepository.getAllDiscardReasons(includeDeleted));
  }
  
  public List<LocationViewModel> getDistributionSites() {
    return locationFactory.createViewModels(locationRepository.getDistributionSites());
  }
  
  public List<LocationViewModel> getProcessingSites() {
    return locationFactory.createViewModels(locationRepository.getProcessingSites());
  }

  public List<LocationViewModel> getUsageSites() {
    return locationFactory.createViewModels(locationRepository.getUsageSites());
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

  public List<TransfusionReactionTypeViewModel> getTransfusionReactionTypes() {
    List<TransfusionReactionType> transfusionReactionTypes = transfusionReactionTypeRepository.getAllTransfusionReactionTypes(false);
    return transfusionReactionTypeFactory.createTransfusionReactionTypeViewModels(transfusionReactionTypes);
  }
  
  public List<BloodTestViewModel> getEnabledTTIBloodTests() {
    return bloodTestFactory.createViewModels(bloodTestRepository.getEnabledBloodTestsOfType(BloodTestType.BASIC_TTI));
  }

 public List<DonationTypeViewModel> getDonationTypes() {
    return donationTypeFactory.createViewModels(donationTypeRepository.getAllDonationTypes());
  }
}

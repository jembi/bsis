package org.jembi.bsis.controllerservice;

import java.util.List;

import org.jembi.bsis.backingform.DeferralBackingForm;
import org.jembi.bsis.factory.DonorDeferralFactory;
import org.jembi.bsis.factory.LocationFactory;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.DonorDeferralCRUDService;
import org.jembi.bsis.viewmodel.DonorDeferralViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DeferralControllerService {

  @Autowired
  private DonorDeferralCRUDService donorDeferralCRUDService;

  @Autowired
  private DonorDeferralFactory deferralFactory;

  @Autowired
  private LocationRepository locationRepository;

  @Autowired
  private LocationFactory locationFactory;

  public DonorDeferralViewModel createDeferral(DeferralBackingForm backingForm) {
    DonorDeferral donorDeferral = deferralFactory.createEntity(backingForm);
    donorDeferral = donorDeferralCRUDService.createDeferral(donorDeferral);
    return deferralFactory.createDonorDeferralViewModel(donorDeferral);
  }

  public List<LocationViewModel> getVenues() {
    return locationFactory.createViewModels(locationRepository.getVenues());
  }

}

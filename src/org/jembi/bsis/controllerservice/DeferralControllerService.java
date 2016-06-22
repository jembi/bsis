package org.jembi.bsis.controllerservice;

import org.jembi.bsis.backingform.DeferralBackingForm;
import org.jembi.bsis.factory.DonorDeferralViewModelFactory;
import org.jembi.bsis.model.donordeferral.DonorDeferral;
import org.jembi.bsis.service.DonorDeferralCRUDService;
import org.jembi.bsis.viewmodel.DonorDeferralViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DeferralControllerService {

  @Autowired
  private DonorDeferralCRUDService donorDeferralCRUDService;

  @Autowired
  private DonorDeferralViewModelFactory deferralViewModelFactory;

  public DonorDeferralViewModel createDeferral(DeferralBackingForm backingForm) {
    DonorDeferral donorDeferral = deferralViewModelFactory.createEntity(backingForm);
    donorDeferral = donorDeferralCRUDService.createDeferral(donorDeferral);
    return deferralViewModelFactory.createDonorDeferralViewModel(donorDeferral);
  }

}

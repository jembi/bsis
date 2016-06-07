package controllerservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backingform.DeferralBackingForm;
import factory.DonorDeferralViewModelFactory;
import model.donordeferral.DonorDeferral;
import service.DonorDeferralCRUDService;
import viewmodel.DonorDeferralViewModel;

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

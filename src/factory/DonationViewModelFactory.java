package factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.donation.Donation;
import service.DonationConstraintChecker;
import service.DonorConstraintChecker;
import viewmodel.AdverseEventViewModel;
import viewmodel.DonationViewModel;

@Service
public class DonationViewModelFactory {

  @Autowired
  private DonationConstraintChecker donationConstraintChecker;
  @Autowired
  private AdverseEventViewModelFactory adverseEventViewModelFactory;
  @Autowired
  private DonorConstraintChecker donorConstraintChecker;

  public List<DonationViewModel> createDonationViewModelsWithPermissions(List<Donation> donations) {
    List<DonationViewModel> donationViewModels = new ArrayList<>();
    for (Donation donation : donations) {
      donationViewModels.add(createDonationViewModelWithPermissions(donation));
    }
    return donationViewModels;
  }

  public List<DonationViewModel> createDonationViewModelsWithoutPermissions(List<Donation> donations) {
    List<DonationViewModel> donationViewModels = new ArrayList<>();
    for (Donation donation : donations) {
      donationViewModels.add(createDonationViewModelWithoutPermissions(donation));
    }
    return donationViewModels;
  }

  public DonationViewModel createDonationViewModelWithPermissions(Donation donation) {
    DonationViewModel donationViewModel = createDonationViewModelWithoutPermissions(donation);

    boolean canDonate = !donorConstraintChecker.isDonorDeferred(donation.getDonor().getId());
    boolean isBackEntry = donation.getDonationBatch().isBackEntry();

    // Populate permissions
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canDelete", donationConstraintChecker.canDeleteDonation(donation.getId()));
    permissions.put("canUpdateDonationFields", donationConstraintChecker.canUpdateDonationFields(donation.getId()));
    permissions.put("canDonate", canDonate);
    permissions.put("isBackEntry", isBackEntry);
    donationViewModel.setPermissions(permissions);

    return donationViewModel;
  }

  public DonationViewModel createDonationViewModelWithoutPermissions(Donation donation) {
    DonationViewModel donationViewModel = new DonationViewModel(donation);

    if (donation.getAdverseEvent() != null) {
      AdverseEventViewModel adverseEventViewModel =
          adverseEventViewModelFactory.createAdverseEventViewModel(donation.getAdverseEvent());
      donationViewModel.setAdverseEvent(adverseEventViewModel);
    }

    return donationViewModel;
  }

}

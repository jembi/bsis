package org.jembi.bsis.factory;

import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.repository.PostDonationCounsellingRepository;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.jembi.bsis.viewmodel.PostDonationCounsellingViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PostDonationCounsellingViewModelFactory {

  @Autowired
  private PostDonationCounsellingRepository postDonationCounsellingRepository;
  @Autowired
  private DonationViewModelFactory donationViewModelFactory;

  public PostDonationCounsellingViewModel createPostDonationCounsellingViewModel(
      PostDonationCounselling postDonationCounselling) {

    PostDonationCounsellingViewModel viewModel = new PostDonationCounsellingViewModel(postDonationCounselling);
    DonationViewModel donationViewModel = donationViewModelFactory.createDonationViewModelWithoutPermissions(
        postDonationCounselling.getDonation());
    viewModel.setDonation(donationViewModel);
    // Populate permissions
    boolean canRemoveStatus = postDonationCounsellingRepository.countNotFlaggedPostDonationCounsellingsForDonor(
        postDonationCounselling.getDonation().getDonor().getId()) > 0;
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canRemoveStatus", canRemoveStatus);
    viewModel.setPermissions(permissions);
    return viewModel;
  }
}

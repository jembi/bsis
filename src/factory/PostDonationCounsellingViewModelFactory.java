package factory;

import model.counselling.PostDonationCounselling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.PostDonationCounsellingRepository;
import viewmodel.PostDonationCounsellingViewModel;

import java.util.HashMap;
import java.util.Map;

@Service
public class PostDonationCounsellingViewModelFactory {

  @Autowired
  private PostDonationCounsellingRepository postDonationCounsellingRepository;

  public PostDonationCounsellingViewModel createPostDonationCounsellingViewModel(
      PostDonationCounselling postDonationCounselling) {

    PostDonationCounsellingViewModel viewModel = new PostDonationCounsellingViewModel(postDonationCounselling);
    // Populate permissions
    boolean canRemoveStatus = postDonationCounsellingRepository.countNotFlaggedPostDonationCounsellingsForDonor(
        postDonationCounselling.getDonation().getDonor().getId()) > 0;
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canRemoveStatus", canRemoveStatus);
    viewModel.setPermissions(permissions);
    return viewModel;
  }
}

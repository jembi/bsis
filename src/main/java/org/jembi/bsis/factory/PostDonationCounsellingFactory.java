package org.jembi.bsis.factory;

import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.backingform.PostDonationCounsellingBackingForm;
import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.repository.PostDonationCounsellingRepository;
import org.jembi.bsis.viewmodel.CounsellingStatusViewModel;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.jembi.bsis.viewmodel.DonorViewModel;
import org.jembi.bsis.viewmodel.PostDonationCounsellingViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostDonationCounsellingFactory {

  @Autowired
  private PostDonationCounsellingRepository postDonationCounsellingRepository;
  @Autowired
  private DonationFactory donationFactory;
  @Autowired
  private DonorViewModelFactory donorFactory;

  public PostDonationCounsellingViewModel createViewModel(PostDonationCounselling postDonationCounselling) {

    PostDonationCounsellingViewModel viewModel = new PostDonationCounsellingViewModel();

    DonationViewModel donationViewModel = donationFactory.createDonationViewModelWithoutPermissions(
        postDonationCounselling.getDonation());
    viewModel.setDonation(donationViewModel);
    viewModel.setId(postDonationCounselling.getId());
    viewModel.setCounsellingDate(postDonationCounselling.getCounsellingDate());
    if (postDonationCounselling.getCounsellingStatus() != null) {
      viewModel.setCounsellingStatus(new CounsellingStatusViewModel(postDonationCounselling.getCounsellingStatus()));
    }
    DonorViewModel donorViewModel = donorFactory.createDonorViewModel(postDonationCounselling.getDonation().getDonor());
    viewModel.setDonor(donorViewModel);
    viewModel.setFlaggedForCounselling(postDonationCounselling.isFlaggedForCounselling());
    viewModel.setNotes(postDonationCounselling.getNotes());
    viewModel.setReferred(postDonationCounselling.getReferred());

    // Populate permissions
    boolean canRemoveStatus = postDonationCounsellingRepository.countNotFlaggedPostDonationCounsellingsForDonor(
        postDonationCounselling.getDonation().getDonor().getId()) > 0;
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canRemoveStatus", canRemoveStatus);
    viewModel.setPermissions(permissions);

    return viewModel;
  }

  public PostDonationCounselling createEntity(PostDonationCounsellingBackingForm form) {
    PostDonationCounselling entity = new PostDonationCounselling();
    entity.setId(form.getId());
    entity.setCounsellingStatus(form.getCounsellingStatus());
    entity.setCounsellingDate(form.getCounsellingDate());
    entity.setFlaggedForCounselling(form.getFlaggedForCounselling());
    entity.setNotes(form.getNotes());
    entity.setReferred(form.isReferred());
    return entity;
  }
}

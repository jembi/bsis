package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.backingform.PostDonationCounsellingBackingForm;
import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.model.counselling.PostDonationCounselling;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donor.Donor;
import org.jembi.bsis.repository.PostDonationCounsellingRepository;
import org.jembi.bsis.viewmodel.CounsellingStatusViewModel;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.jembi.bsis.viewmodel.DonorViewModel;
import org.jembi.bsis.viewmodel.PostDonationCounsellingSummaryViewModel;
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
  @Autowired
  private LocationFactory locationFactory;

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

  public List<PostDonationCounsellingSummaryViewModel> createSummaryViewModels(List<PostDonationCounselling> entities) {
    List<PostDonationCounsellingSummaryViewModel> viewModels = new ArrayList<>();
    if (entities != null) {
      for (PostDonationCounselling entity : entities) {
        viewModels.add(createSummaryViewModel(entity));
      }
    }
    return viewModels;
  }

  public PostDonationCounsellingSummaryViewModel createSummaryViewModel(
      PostDonationCounselling postDonationCounselling) {

    String counselled = "";
    CounsellingStatus status = postDonationCounselling.getCounsellingStatus();
    if (status != null) {
      if (status.equals(CounsellingStatus.RECEIVED_COUNSELLING)) {
        counselled = "Y";
      } else if (status.equals(CounsellingStatus.REFUSED_COUNSELLING)) {
        counselled = "R";
      } else if (status.equals(CounsellingStatus.DID_NOT_RECEIVE_COUNSELLING)) {
        counselled = "N";
      }
    }

    Donation donation = postDonationCounselling.getDonation();
    Donor donor = donation.getDonor();

    PostDonationCounsellingSummaryViewModel viewModel = new PostDonationCounsellingSummaryViewModel();
    viewModel.setCounselled(counselled);
    viewModel.setReferred(postDonationCounselling.getReferred() == true ? "Y" : "N");
    viewModel.setCounsellingDate(postDonationCounselling.getCounsellingDate());
    viewModel.setDonorNumber(donation.getDonorNumber());
    viewModel.setFirstName(donor.getFirstName());
    viewModel.setLastName(donor.getLastName());
    viewModel.setGender(donor.getGender().name());
    viewModel.setBirthDate(donor.getBirthDate());
    viewModel.setBloodAbo(donation.getBloodAbo());
    viewModel.setBloodRh(donation.getBloodRh());
    viewModel.setDonationIdentificationNumber(donation.getDonationIdentificationNumber());
    viewModel.setDonationDate(donation.getDonationDate());
    viewModel.setVenue(locationFactory.createViewModel(donation.getVenue()));
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

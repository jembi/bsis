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
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.repository.PostDonationCounsellingRepository;
import org.jembi.bsis.viewmodel.DonationFullViewModel;
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
  @Autowired
  private LocationRepository locationRepository;

  public PostDonationCounsellingViewModel createViewModel(PostDonationCounselling postDonationCounselling) {

    PostDonationCounsellingViewModel viewModel = new PostDonationCounsellingViewModel();

    DonationFullViewModel donationFullViewModel = donationFactory.createDonationFullViewModelWithoutPermissions(
        postDonationCounselling.getDonation());
    viewModel.setDonation(donationFullViewModel);
    viewModel.setId(postDonationCounselling.getId());
    viewModel.setCounsellingDate(postDonationCounselling.getCounsellingDate());
    viewModel.setCounsellingStatus(postDonationCounselling.getCounsellingStatus());
    DonorViewModel donorViewModel = donorFactory.createDonorViewModel(postDonationCounselling.getDonation().getDonor());
    viewModel.setDonor(donorViewModel);
    viewModel.setFlaggedForCounselling(postDonationCounselling.isFlaggedForCounselling());
    viewModel.setNotes(postDonationCounselling.getNotes());
    viewModel.setReferred(postDonationCounselling.getReferred());
    if (postDonationCounselling.getReferralSite() != null) {
      viewModel.setReferralSite(locationFactory.createViewModel(postDonationCounselling.getReferralSite()));
    }

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
    
    String referred = "";
    if (postDonationCounselling.getReferred() != null) {
      referred = postDonationCounselling.getReferred() == true ? "Y" : "N";
    }

    Donation donation = postDonationCounselling.getDonation();
    Donor donor = donation.getDonor();

    PostDonationCounsellingSummaryViewModel viewModel = new PostDonationCounsellingSummaryViewModel();
    viewModel.setId(postDonationCounselling.getId());
    viewModel.setCounselled(counselled);
    viewModel.setReferred(referred);
    viewModel.setCounsellingDate(postDonationCounselling.getCounsellingDate());
    viewModel.setDonorNumber(donor.getDonorNumber());
    viewModel.setFirstName(donor.getFirstName());
    viewModel.setLastName(donor.getLastName());
    viewModel.setGender(donor.getGender());
    viewModel.setBirthDate(donor.getBirthDate());
    viewModel.setBloodGroup(donation.getBloodAbo() + donation.getBloodRh());
    viewModel.setDonationIdentificationNumber(donation.getDonationIdentificationNumber());
    viewModel.setDonationDate(donation.getDonationDate());
    viewModel.setVenue(locationFactory.createViewModel(donation.getVenue()));
    
    viewModel.setDonorId(donor.getId());
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
    if (form.getReferralSite() != null) {
      entity.setReferralSite(locationRepository.getLocation(form.getReferralSite().getId()));
    }
    return entity;
  }
}

package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.viewmodel.DonationFullViewModel;
import org.jembi.bsis.viewmodel.DonorViewModel;
import org.jembi.bsis.viewmodel.LocationViewModel;
import org.jembi.bsis.viewmodel.PostDonationCounsellingViewModel;

public class PostDonationCounsellingViewModelBuilder extends AbstractBuilder<PostDonationCounsellingViewModel> {

  private UUID id;
  private boolean flaggedForCounselling = false;
  private CounsellingStatus counsellingStatus;
  private Date counsellingDate;
  private DonorViewModel donor;
  private String notes;
  private DonationFullViewModel donation;
  private Map<String, Boolean> permissions;
  private Boolean referred;
  private LocationViewModel referralSite;
  
  public PostDonationCounsellingViewModelBuilder withId(UUID id) {
    this.id = id;
    return this;
  }
  
  public PostDonationCounsellingViewModelBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public PostDonationCounsellingViewModelBuilder thatIsFlaggedForCounselling() {
    this.flaggedForCounselling = true;
    return this;
  }

  public PostDonationCounsellingViewModelBuilder thatIsNotFlaggedForCounselling() {
    this.flaggedForCounselling = false;
    return this;
  }

  public PostDonationCounsellingViewModelBuilder withCounsellingDate(Date counsellingDate) {
    this.counsellingDate = counsellingDate;
    return this;
  }

  public PostDonationCounsellingViewModelBuilder withDonor(DonorViewModel donor) {
    this.donor = donor;
    return this;
  }

  public PostDonationCounsellingViewModelBuilder withDonation(DonationFullViewModel donation) {
    this.donation = donation;
    return this;
  }

  public PostDonationCounsellingViewModelBuilder withCounsellingStatus(CounsellingStatus counsellingStatus) {
    this.counsellingStatus = counsellingStatus;
    return this;
  }

  public PostDonationCounsellingViewModelBuilder withReferred(Boolean referred) {
    this.referred = referred;
    return this;
  }

  public PostDonationCounsellingViewModelBuilder thatIsReferred() {
    this.referred = true;
    return this;
  }

  public PostDonationCounsellingViewModelBuilder thatIsNotReferred() {
    this.referred = false;
    return this;
  }
  
  public PostDonationCounsellingViewModelBuilder withReferralSite(LocationViewModel referralSite) {
    this.referralSite = referralSite;
    return this;
  }

  public PostDonationCounsellingViewModelBuilder withPermission(String key, boolean value) {
    if (permissions == null) {
      permissions = new HashMap<>();
    }
    permissions.put(key, value);
    return this;
  }
  
  

  @Override
  public PostDonationCounsellingViewModel build() {
    PostDonationCounsellingViewModel viewModel = new PostDonationCounsellingViewModel();
    viewModel.setPermissions(permissions);
    viewModel.setDonation(donation);
    viewModel.setFlaggedForCounselling(flaggedForCounselling);
    viewModel.setId(id);
    viewModel.setCounsellingDate(counsellingDate);
    viewModel.setCounsellingStatus(counsellingStatus);
    viewModel.setDonor(donor);
    viewModel.setNotes(notes);
    viewModel.setReferred(referred);
    viewModel.setReferralSite(referralSite);
    return viewModel;
  }

  public static PostDonationCounsellingViewModelBuilder aPostDonationCounsellingViewModel() {
    return new PostDonationCounsellingViewModelBuilder();
  }

}

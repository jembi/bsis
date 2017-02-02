package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.viewmodel.CounsellingStatusViewModel;
import org.jembi.bsis.viewmodel.DonationViewModel;
import org.jembi.bsis.viewmodel.DonorViewModel;
import org.jembi.bsis.viewmodel.PostDonationCounsellingViewModel;

public class PostDonationCounsellingViewModelBuilder extends AbstractBuilder<PostDonationCounsellingViewModel> {

  private long id;
  private boolean flaggedForCounselling = false;
  private CounsellingStatusViewModel counsellingStatus;
  private Date counsellingDate;
  private DonorViewModel donor;
  private String notes;
  private DonationViewModel donation;
  private Map<String, Boolean> permissions;
  
  public PostDonationCounsellingViewModelBuilder withId(long id) {
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

  public PostDonationCounsellingViewModelBuilder withCounsellingDate(Date counsellingDate) {
    this.counsellingDate = counsellingDate;
    return this;
  }

  public PostDonationCounsellingViewModelBuilder withDonor(DonorViewModel donor) {
    this.donor = donor;
    return this;
  }

  public PostDonationCounsellingViewModelBuilder withDonation(DonationViewModel donation) {
    this.donation = donation;
    return this;
  }

  public PostDonationCounsellingViewModelBuilder withCounsellingStatusViewModel(
      CounsellingStatusViewModel counsellingStatus) {
    this.counsellingStatus = counsellingStatus;
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
    return viewModel;
  }

  public static PostDonationCounsellingViewModelBuilder aPostDonationCounsellingViewModel() {
    return new PostDonationCounsellingViewModelBuilder();
  }

}

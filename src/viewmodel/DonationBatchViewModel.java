package viewmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.user.User;
import utils.CustomDateFormatter;

public class DonationBatchViewModel {

  private DonationBatch donationBatch;

  public DonationBatchViewModel() {
  }
  
  public DonationBatchViewModel(DonationBatch donationBatch) {
    this.donationBatch = donationBatch;
  }

  public Integer getId() {
    return donationBatch.getId();
  }

  public String getBatchNumber() {
    return donationBatch.getBatchNumber();
  }

  public String getNotes() {
    return donationBatch.getNotes();
  }

  public List<DonationViewModel> getDonations() {
    if (donationBatch.getDonations() == null)
      return Arrays.asList(new DonationViewModel[0]);
    List<DonationViewModel> collectionViewModels = new ArrayList<DonationViewModel>();
    for (Donation collection : donationBatch.getDonations()) {
      collectionViewModels.add(new DonationViewModel(collection));
    }
    return collectionViewModels;
  }
  
  public Integer getNumCollections() {
	 return donationBatch.getDonations().size();
  }

  public String getLastUpdated() {
    return CustomDateFormatter.getDateTimeString(donationBatch.getLastUpdated());
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateString(donationBatch.getCreatedDate());
  }

  public String getCreatedBy() {
    User user = donationBatch.getCreatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getLastUpdatedBy() {
    User user = donationBatch.getLastUpdatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }
  
  public Boolean getIsClosed() {
    return donationBatch.getIsClosed();
  }
  
  public LocationViewModel getDonorPanel(){
      return  new LocationViewModel((donationBatch.getDonorPanel()));
  }
}

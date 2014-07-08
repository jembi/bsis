package viewmodel;

import java.util.Date;
import java.util.List;
import model.collectedsample.CollectedSample;
import model.donationbatch.DonationBatch;
import model.location.Location;
import model.user.User;
import utils.CustomDateFormatter;

public class DonationBatchViewModel {

  private DonationBatch donationBatch;
  private Long numberOfDonations; 

  public DonationBatchViewModel(DonationBatch donationBatch, Long numberOfDonations) {
    this.donationBatch = donationBatch;
    this.numberOfDonations = numberOfDonations;
  }
  
    public DonationBatchViewModel(DonationBatch donationBatch) {
    this.donationBatch = donationBatch;
  }

  public DonationBatch getCollectionBatch() {
    return donationBatch;
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

  public List<CollectedSample> getCollectionsInBatch() {
    return donationBatch.getDonationsInBatch();
  }

  public Location getDonationCenter() {
    return donationBatch.getDonationCenter();
  }

  public Location getDonationSite() {
    return donationBatch.getDonationSite();
  }

  public String getLastUpdated() {
    return CustomDateFormatter.getDateTimeString(donationBatch.getLastUpdated());
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateTimeString(donationBatch.getCreatedDate());
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
  
  public Long getNumberOfDonations(){
      return numberOfDonations;
  }
  
  public Date getBatchOpenedOn(){
     return  donationBatch.getBatchOpenedOn();
  }
  public Date getBatchClosedOn(){
      return donationBatch.getBatchClosedOn();
  }
}

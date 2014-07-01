package viewmodel;

import java.util.List;

import utils.CustomDateFormatter;

import model.collectedsample.CollectedSample;
import model.donationbatch.DonationBatch;
import model.location.Location;
import model.user.User;

public class DonationBatchViewModel {

  private DonationBatch donationBatch;

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
}

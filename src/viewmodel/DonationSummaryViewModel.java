package viewmodel;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import model.donation.Donation;
import repository.bloodtesting.BloodTypingMatchStatus;
import repository.bloodtesting.BloodTypingStatus;
import utils.DateTimeSerialiser;

public class DonationSummaryViewModel {

  private boolean includeVenueInfo = true;
  private boolean includeDonorInfo = true;

  private Donation donation;

  public DonationSummaryViewModel(Donation donation) {
    this.donation = donation;
  }

  public DonationSummaryViewModel(Donation donation, boolean includeTestResults, boolean includeVenueInfo,
      boolean includeDonorInfo) {
    this.includeDonorInfo = includeDonorInfo;
    this.includeVenueInfo = includeVenueInfo;

    if (includeTestResults == false) {
      donation.setBloodTestResults(null);
    }
    this.donation = donation;
  }

  public String getDonationIdentificationNumber() {
    return donation.getDonationIdentificationNumber();
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getDonationDate() {
    return donation.getDonationDate();
  }

  public String getBloodAbo() {
    return donation.getBloodAbo();
  }

  public String getBloodRh() {
    return donation.getBloodRh();
  }

  public BloodTypingMatchStatus getBloodTypingMatchStatus() {
    return donation.getBloodTypingMatchStatus();
  }

  public BloodTypingStatus getBloodTypingStatus() {
    return donation.getBloodTypingStatus();
  }

  public LocationViewModel getVenue() {
    if (includeVenueInfo) {
      return new LocationViewModel(donation.getVenue());
    } else {
      return null;
    }
  }

  public DonorViewModel getDonor() {
    if (includeDonorInfo) {
      return new DonorViewModel(donation.getDonor());
    } else {
      return null;
    }
  }

}

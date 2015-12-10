package viewmodel;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import model.donation.Donation;
import utils.DateTimeSerialiser;

import java.util.Date;

public class DonationSummaryViewModel {

  private Donation donation;

  public DonationSummaryViewModel(Donation donation) {
    this.donation = donation;
  }

  public String getDonationIdentificationNumber() {
    return donation.getDonationIdentificationNumber();
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getDonationDate() {
    return donation.getDonationDate();
  }

  public LocationViewModel getVenue() {
    return new LocationViewModel(donation.getVenue());
  }

  public DonorViewModel getDonor() {
    return new DonorViewModel(donation.getDonor());
  }

}

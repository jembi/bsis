package org.jembi.bsis.viewmodel;

import java.util.Date;

import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * The Class DonationSummaryViewModel.
 */
public class DonationSummaryViewModel {

  /** The include venue info. */
  private boolean includeVenueInfo = false;

  /** The include donor info. */
  private boolean includeDonorInfo = false;

  /** The donation. */
  private Donation donation;

  /**
   * Instantiates a new donation summary view model (doesn't include venue and donor info).
   *
   * @param donation the donation
   */
  public DonationSummaryViewModel(Donation donation) {
    this.donation = donation;
  }

  /**
   * Instantiates a new donation summary view model. This constructor allows to add venue and donor
   * info to the summary.
   *
   * @param donation the donation
   * @param includeVenueInfo the include venue info
   * @param includeDonorInfo the include donor info
   */
  public DonationSummaryViewModel(Donation donation, boolean includeVenueInfo, boolean includeDonorInfo) {
    this.includeDonorInfo = includeDonorInfo;
    this.includeVenueInfo = includeVenueInfo;
    this.donation = donation;
  }

  public Long getId() {
    return donation.getId();
  }

  /**
   * Gets the donation identification number.
   *
   * @return the donation identification number
   */
  public String getDonationIdentificationNumber() {
    return donation.getDonationIdentificationNumber();
  }

  /**
   * Gets the donation date.
   *
   * @return the donation date
   */
  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getDonationDate() {
    return donation.getDonationDate();
  }

  /**
   * Gets the blood abo.
   *
   * @return the blood abo
   */
  public String getBloodAbo() {
    return donation.getBloodAbo();
  }

  /**
   * Gets the blood rh.
   *
   * @return the blood rh
   */
  public String getBloodRh() {
    return donation.getBloodRh();
  }

  /**
   * Gets the blood typing match status.
   *
   * @return the blood typing match status
   */
  public BloodTypingMatchStatus getBloodTypingMatchStatus() {
    return donation.getBloodTypingMatchStatus();
  }

  /**
   * Gets the blood typing status.
   *
   * @return the blood typing status
   */
  public BloodTypingStatus getBloodTypingStatus() {
    return donation.getBloodTypingStatus();
  }

  /**
   * Gets the venue.
   *
   * @return the venue
   */
  public LocationFullViewModel getVenue() {
    if (includeVenueInfo) {
      return new LocationFullViewModel(donation.getVenue());
    } else {
      return null;
    }
  }

  /**
   * Gets the donor.
   *
   * @return the donor
   */
  public DonorViewModel getDonor() {
    if (includeDonorInfo) {
      return new DonorViewModel(donation.getDonor());
    } else {
      return null;
    }
  }

}

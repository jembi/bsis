package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.utils.CustomDateFormatter;

public class DonationViewModel extends BaseViewModel<UUID> {

  private String donorNumber;
  private String donationIdentificationNumber;
  private PackTypeViewModel packType;
  private DonationTypeViewModel donationType;
  private Date donationDate;
  private LocationViewModel venue;
  private boolean released;
  private TTIStatus ttiStatus;
  private BloodTypingStatus bloodTypingStatus;
  private BloodTypingMatchStatus bloodTypingMatchStatus;

  public DonationViewModel() {
  }
  
  public void setDonationDate(Date donationDate) {
    this.donationDate = donationDate;
  }
  
  public String getDonationDate() {
    return CustomDateFormatter.getDateTimeString(donationDate);
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }
  
  public void setDonationType(DonationTypeViewModel donationType) {
    this.donationType = donationType;
  }

  public DonationTypeViewModel getDonationType() {
    return donationType;
  }

  public void setPackType(PackTypeViewModel packType) {
    this.packType = packType;
  }

  public PackTypeViewModel getPackType() {
    return packType;
  }

  public void setTTIStatus(TTIStatus ttiStatus) {
    this.ttiStatus = ttiStatus;
  }

  public String getTTIStatus() {
    if (ttiStatus == null) {
      return "";
    }
    return ttiStatus.toString();
  }
  
  public void setBloodTypingStatus(BloodTypingStatus bloodTypingStatus) {
    this.bloodTypingStatus = bloodTypingStatus;
  }

  public String getBloodTypingStatus() {
    if (bloodTypingStatus == null) {
      return "";
    }
    return bloodTypingStatus.toString();
  }
  
  public void setBloodTypingMatchStatus(BloodTypingMatchStatus bloodTypingMatchStatus) {
    this.bloodTypingMatchStatus = bloodTypingMatchStatus;
  }

  public String getBloodTypingMatchStatus() {
    if (bloodTypingMatchStatus == null) {
      return "";
    }
    return bloodTypingMatchStatus.toString();
  }

  public void setVenue(LocationViewModel venue) {
    this.venue = venue;
  }

  public LocationViewModel getVenue() {
    return venue;
  }

  public String getDonorNumber() {
    return donorNumber;
  }

  public void setDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
  }

  public boolean isReleased() {
    return released;
  }

  public void setReleased(boolean released) {
    this.released = released;
  }
}

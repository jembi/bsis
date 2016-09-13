package org.jembi.bsis.dto;

import java.util.Date;

public class PostDonationCounsellingExportDTO extends ModificationTrackerExportDTO {

  private String donationIdentificationNumber;
  private Date counsellingDate;
  
  public PostDonationCounsellingExportDTO() {
    // Default constructor
  }

  public PostDonationCounsellingExportDTO(String donationIdentificationNumber, Date createdDate, String createdBy,
      Date lastUpdated, String lastUpdatedBy, Date counsellingDate) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    this.createdDate = createdDate;
    this.createdBy = createdBy;
    this.lastUpdated = lastUpdated;
    this.lastUpdatedBy = lastUpdatedBy;
    this.counsellingDate = counsellingDate;
  }

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public Date getCounsellingDate() {
    return counsellingDate;
  }

  public void setCounsellingDate(Date counsellingDate) {
    this.counsellingDate = counsellingDate;
  }
}

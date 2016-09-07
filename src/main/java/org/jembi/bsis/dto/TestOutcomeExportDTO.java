package org.jembi.bsis.dto;

import java.util.Date;

public class TestOutcomeExportDTO extends ModificationTrackerExportDTO {

  private String donationIdentificationNumber;
  private String outcome;
  
  public TestOutcomeExportDTO(String donationIdentificationNumber, Date createdDate, String createdBy, Date lastUpdated,
      String lastUpdatedBy, String outcome) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    this.createdDate = createdDate;
    this.createdBy = createdBy;
    this.lastUpdated = lastUpdated;
    this.lastUpdatedBy = lastUpdatedBy;
    this.outcome = outcome;
  }

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public String getOutcome() {
    return outcome;
  }

  public void setOutcome(String outcome) {
    this.outcome = outcome;
  }

}

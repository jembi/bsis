package org.jembi.bsis.dto;

import java.util.Date;

public class BloodTestResultExportDTO extends ModificationTrackerExportDTO {

  private String donationIdentificationNumber;
  private String result;
  private String testName;
  
  public BloodTestResultExportDTO() {
    // Default constructor
  }

  public BloodTestResultExportDTO(String donationIdentificationNumber, Date createdDate, String createdBy,
      Date lastUpdated, String lastUpdatedBy, String testName, String result) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    this.createdDate = createdDate;
    this.createdBy = createdBy;
    this.lastUpdated = lastUpdated;
    this.lastUpdatedBy = lastUpdatedBy;
    this.testName = testName;
    this.result = result;
  }

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public String getTestName() {
    return testName;
  }

  public void setTestName(String testName) {
    this.testName = testName;
  }

}

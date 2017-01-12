package org.jembi.bsis.dto;

import java.util.Date;

public class DeferralExportDTO extends ModificationTrackerExportDTO {

  private String donorNumber;
  private String deferralReasonText;
  private String deferralReason;
  private Date deferralDate;
  private Date deferredUntil;
  
  public DeferralExportDTO() {
    // Default constructor
  }
  
  public DeferralExportDTO(String donorNumber, Date createdDate, String createdBy, Date lastUpdated,
      String lastUpdatedBy, String deferralReasonText, String deferralReason, Date deferralDate, Date deferredUntil) {
    this.donorNumber = donorNumber;
    this.createdDate = createdDate;
    this.createdBy = createdBy;
    this.lastUpdated = lastUpdated;
    this.lastUpdatedBy = lastUpdatedBy;
    this.deferralReasonText = deferralReasonText;
    this.deferralReason = deferralReason;
    this.deferralDate = deferralDate;
    this.deferredUntil = deferredUntil;
  }

  public String getDonorNumber() {
    return donorNumber;
  }

  public void setDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
  }

  public String getDeferralReasonText() {
    return deferralReasonText;
  }

  public void setDeferralReasonText(String deferralReasonText) {
    this.deferralReasonText = deferralReasonText;
  }

  public String getDeferralReason() {
    return deferralReason;
  }

  public void setDeferralReason(String deferralReason) {
    this.deferralReason = deferralReason;
  }

  public Date getDeferralDate() {
    return deferralDate;
  }

  public void setDeferralDate(Date deferralDate) {
    this.deferralDate = deferralDate;
  }

  public Date getDeferredUntil() {
    return deferredUntil;
  }

  public void setDeferredUntil(Date deferredUntil) {
    this.deferredUntil = deferredUntil;
  }

}

package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.Map;

import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class DonorDeferralViewModel {

  private Map<String, Boolean> permissions;
  private Long id;
  private Date deferredUntil;
  private DeferralReasonViewModel deferralReason;
  private String createdBy;
  private String donorNumber;
  private LocationViewModel venue;
  private Date deferralDate;
  private DonorViewModel deferredDonor;
  private String deferralReasonText;

  public Map<String, Boolean> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getDeferredUntil() {
    return deferredUntil;
  }

  public void setDeferredUntil(Date deferredUntil) {
    this.deferredUntil = deferredUntil;
  }

  public DeferralReasonViewModel getDeferralReason() {
    return deferralReason;
  }

  public void setDeferralReason(DeferralReasonViewModel deferralReason) {
    this.deferralReason = deferralReason;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getDonorNumber() {
    return donorNumber;
  }

  public void setDonorNumber(String donorNumber) {
    this.donorNumber = donorNumber;
  }

  public LocationViewModel getVenue() {
    return venue;
  }

  public void setVenue(LocationViewModel venue) {
    this.venue = venue;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getDeferralDate() {
    return deferralDate;
  }

  public void setDeferralDate(Date deferralDate) {
    this.deferralDate = deferralDate;
  }

  public DonorViewModel getDeferredDonor() {
    return deferredDonor;
  }

  public void setDeferredDonor(DonorViewModel deferredDonor) {
    this.deferredDonor = deferredDonor;
  }

  public String getDeferralReasonText() {
    return deferralReasonText;
  }

  public void setDeferralReasonText(String deferralReasonText) {
    this.deferralReasonText = deferralReasonText;
  }

}

package org.jembi.bsis.backingform;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class DeferralBackingForm {

  private UUID id;

  private DonorBackingForm deferredDonor;

  private DeferralReasonBackingForm deferralReason;

  private String deferralReasonText;

  private Date deferredUntil;

  private LocationBackingForm venue;

  private Date deferralDate;

  public DonorBackingForm getDeferredDonor() {
    return deferredDonor;
  }

  public void setDeferredDonor(DonorBackingForm deferredDonor) {
    this.deferredDonor = deferredDonor;
  }

  public DeferralReasonBackingForm getDeferralReason() {
    return deferralReason;
  }

  public void setDeferralReason(DeferralReasonBackingForm deferralReason) {
    this.deferralReason = deferralReason;
  }

  public String getDeferralReasonText() {
    return deferralReasonText;
  }

  public void setDeferralReasonText(String deferralReasonText) {
    this.deferralReasonText = deferralReasonText;
  }

  public Date getDeferredUntil() {
    return deferredUntil;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public void setDeferredUntil(Date deferredUntil) {
    this.deferredUntil = deferredUntil;
  }

  public LocationBackingForm getVenue() {
    return venue;
  }

  public void setVenue(LocationBackingForm venue) {
    this.venue = venue;
  }

  public Date getDeferralDate() {
    return deferralDate;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public void setDeferralDate(Date deferralDate) {
    this.deferralDate = deferralDate;
  }

  @JsonIgnore
  public void setPermissions(Map<String, Boolean> permissions) {
    // Ignore
  }

  @JsonIgnore
  public void setCreatedBy(String createdBy) {
    // Ignore
  }

  @JsonIgnore
  public void setDonorNumber(String donorNumber) {
    // Ignore
  }

}

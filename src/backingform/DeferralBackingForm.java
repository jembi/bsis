package backingform;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import utils.DateTimeSerialiser;

public class DeferralBackingForm {

  private Long id;

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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

}

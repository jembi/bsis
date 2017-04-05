package org.jembi.bsis.backingform;

import java.util.Date;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class PostDonationCounsellingBackingForm {

  @NotNull
  private UUID id;
  private CounsellingStatus counsellingStatus;
  private Date counsellingDate;
  private String notes;
  private boolean flaggedForCounselling;
  private Boolean referred;
  private LocationBackingForm referralSite;
  
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public CounsellingStatus getCounsellingStatus() {
    return counsellingStatus;
  }

  public void setCounsellingStatus(CounsellingStatus counsellingStatus) {
    this.counsellingStatus = counsellingStatus;
  }

  public Date getCounsellingDate() {
    return counsellingDate;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public void setCounsellingDate(Date counsellingDate) {
    this.counsellingDate = counsellingDate;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setFlaggedForCounselling(boolean flaggedForCounselling) {
    this.flaggedForCounselling = flaggedForCounselling;
  }

  public boolean getFlaggedForCounselling() {
    return flaggedForCounselling;
  }

  public Boolean isReferred() {
    return referred;
  }

  public void setReferred(Boolean referred) {
    this.referred = referred;
  }
   
  public LocationBackingForm getReferralSite() {
    return referralSite;
  }

  public void setReferralSite(LocationBackingForm referralSite) {
    this.referralSite = referralSite;
  }
}


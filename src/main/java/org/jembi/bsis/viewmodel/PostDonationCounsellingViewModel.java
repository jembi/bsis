package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.model.counselling.CounsellingStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class PostDonationCounsellingViewModel {

  private UUID id;

  private boolean flaggedForCounselling;

  private CounsellingStatus counsellingStatus;

  private Date counsellingDate;

  private Map<String, Boolean> permissions;

  private DonationFullViewModel donation;

  private DonorViewModel donor;

  private String notes;

  private Boolean referred;

  private LocationViewModel referralSite;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public boolean isFlaggedForCounselling() {
    return flaggedForCounselling;
  }

  public void setFlaggedForCounselling(boolean flaggedForCounselling) {
    this.flaggedForCounselling = flaggedForCounselling;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getCounsellingDate() {
    return counsellingDate;
  }

  public void setCounsellingDate(Date counsellingDate) {
    this.counsellingDate = counsellingDate;
  }

  public CounsellingStatus getCounsellingStatus() {
    return counsellingStatus;
  }

  public void setCounsellingStatus(CounsellingStatus counsellingStatus) {
    this.counsellingStatus = counsellingStatus;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setDonation(DonationFullViewModel donation) {
    this.donation = donation;
  }

  public DonationFullViewModel getDonation() {
    return donation;
  }

  public DonorViewModel getDonor() {
    return donor;
  }

  public void setDonor(DonorViewModel donor) {
    this.donor = donor;
  }

  public Map<String, Boolean> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }

  public Boolean isReferred() {
    return referred;
  }

  public void setReferred(Boolean referred) {
    this.referred = referred;
  }

  public LocationViewModel getReferralSite() {
    return referralSite;
  }

  public void setReferralSite(LocationViewModel referralSite) {
    this.referralSite = referralSite;
  }
}

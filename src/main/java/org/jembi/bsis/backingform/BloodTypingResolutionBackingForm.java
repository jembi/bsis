package org.jembi.bsis.backingform;

import java.util.UUID;

import org.jembi.bsis.model.donation.BloodTypingMatchStatus;

public class BloodTypingResolutionBackingForm {
  
  private UUID donationId;

  private String bloodAbo;

  private String bloodRh;

  private BloodTypingMatchStatus status;

  public BloodTypingResolutionBackingForm() {
    super();
  }

  public UUID getDonationId() {
    return donationId;
  }

  public void setDonationId(UUID donationId) {
    this.donationId = donationId;
  }

  public String getBloodAbo() {
    return bloodAbo;
  }

  public void setBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
  }

  public String getBloodRh() {
    return bloodRh;
  }

  public void setBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
  }

  public BloodTypingMatchStatus getStatus() {
    return status;
  }

  public void setStatus(BloodTypingMatchStatus status) {
    this.status = status;
  }

}

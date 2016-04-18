package backingform;

import repository.bloodtesting.BloodTypingMatchStatus;

public class BloodTypingResolutionBackingForm {
  
  private long donationId;

  private String bloodAbo;

  private String bloodRh;

  private BloodTypingMatchStatus status;

  public BloodTypingResolutionBackingForm() {
    super();
  }

  public long getDonationId() {
    return donationId;
  }

  public void setDonationId(long donationId) {
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

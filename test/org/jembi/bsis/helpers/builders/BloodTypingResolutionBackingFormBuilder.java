package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.backingform.BloodTypingResolutionBackingForm;
import org.jembi.bsis.repository.bloodtesting.BloodTypingMatchStatus;

public class BloodTypingResolutionBackingFormBuilder extends AbstractBuilder<BloodTypingResolutionBackingForm> {
  
  private long donationId;
  private BloodTypingMatchStatus status;
  private String bloodAbo;
  private String bloodRh;

  public BloodTypingResolutionBackingFormBuilder withDonationId(long donationId) {
    this.donationId = donationId;
    return this;
  }
  
  public BloodTypingResolutionBackingFormBuilder withStatus(BloodTypingMatchStatus status) {
    this.status = status;
    return this;
  }
  
  public BloodTypingResolutionBackingFormBuilder withBloodAbo(String bloodAbo) {
    this.bloodAbo = bloodAbo;
    return this;
  }
  
  public BloodTypingResolutionBackingFormBuilder withBloodRh(String bloodRh) {
    this.bloodRh = bloodRh;
    return this;
  }

  @Override
  public BloodTypingResolutionBackingForm build() {
    BloodTypingResolutionBackingForm backingForm = new BloodTypingResolutionBackingForm();
    backingForm.setDonationId(donationId);
    backingForm.setStatus(status);
    backingForm.setBloodAbo(bloodAbo);
    backingForm.setBloodRh(bloodRh);
    return backingForm;
  }
  
  public static BloodTypingResolutionBackingFormBuilder aBloodTypingResolutionBackingForm() {
    return new BloodTypingResolutionBackingFormBuilder();
  }

}

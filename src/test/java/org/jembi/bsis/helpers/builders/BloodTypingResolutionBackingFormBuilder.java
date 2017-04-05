package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.backingform.BloodTypingResolutionBackingForm;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;

public class BloodTypingResolutionBackingFormBuilder extends AbstractBuilder<BloodTypingResolutionBackingForm> {
  
  private UUID donationId;
  private BloodTypingMatchStatus status;
  private String bloodAbo;
  private String bloodRh;

  public BloodTypingResolutionBackingFormBuilder withDonationId(UUID donationId) {
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

package org.jembi.bsis.helpers.builders;

import java.util.Date;

import org.jembi.bsis.backingform.DeferralBackingForm;
import org.jembi.bsis.backingform.DeferralReasonBackingForm;
import org.jembi.bsis.backingform.DonorBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;

public class DeferralBackingFormBuilder extends AbstractBuilder<DeferralBackingForm> {
  
  private DeferralReasonBackingForm deferralReason;
  private LocationBackingForm venue;
  private Date deferredUntil;
  private DonorBackingForm deferredDonor;
  private Date deferralDate;
  private String deferralReasonText;

  public DeferralBackingFormBuilder withDeferralReason(DeferralReasonBackingForm deferralReason) {
    this.deferralReason = deferralReason;
    return this;
  }
  
  public DeferralBackingFormBuilder withVenue(LocationBackingForm venue) {
    this.venue = venue;
    return this;
  }
  
  public DeferralBackingFormBuilder withDeferredUntil(Date deferredUntil) {
    this.deferredUntil = deferredUntil;
    return this;
  }
  
  public DeferralBackingFormBuilder withDeferredDonor(DonorBackingForm deferredDonor) {
    this.deferredDonor = deferredDonor;
    return this;
  }
  
  public DeferralBackingFormBuilder withDeferralDate(Date deferralDate) {
    this.deferralDate = deferralDate;
    return this;
  }
  
  public DeferralBackingFormBuilder withDeferralReasonText(String deferralReasonText) {
    this.deferralReasonText = deferralReasonText;
    return this;
  }

  @Override
  public DeferralBackingForm build() {
    DeferralBackingForm deferralBackingForm = new DeferralBackingForm();
    deferralBackingForm.setDeferralReason(deferralReason);
    deferralBackingForm.setVenue(venue);
    deferralBackingForm.setDeferredUntil(deferredUntil);
    deferralBackingForm.setDeferredDonor(deferredDonor);
    deferralBackingForm.setDeferralDate(deferralDate);
    deferralBackingForm.setDeferralReasonText(deferralReasonText);
    return deferralBackingForm;
  }
  
  public static DeferralBackingFormBuilder aDeferralBackingForm() {
    return new DeferralBackingFormBuilder();
  }

}

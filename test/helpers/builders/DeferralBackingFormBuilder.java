package helpers.builders;

import backingform.DeferralBackingForm;
import model.donordeferral.DeferralReason;
import model.location.Location;

public class DeferralBackingFormBuilder extends AbstractBuilder<DeferralBackingForm> {
  
  private DeferralReason deferralReason;
  private Location venue;

  public DeferralBackingFormBuilder withDeferralReason(DeferralReason deferralReason) {
    this.deferralReason = deferralReason;
    return this;
  }
  
  public DeferralBackingFormBuilder withVenue(Location venue) {
    this.venue = venue;
    return this;
  }

  @Override
  public DeferralBackingForm build() {
    DeferralBackingForm deferralBackingForm = new DeferralBackingForm();
    deferralBackingForm.setDeferralReason(deferralReason);
    deferralBackingForm.setVenue(venue);
    return deferralBackingForm;
  }
  
  public static DeferralBackingFormBuilder aDeferralBackingForm() {
    return new DeferralBackingFormBuilder();
  }

}

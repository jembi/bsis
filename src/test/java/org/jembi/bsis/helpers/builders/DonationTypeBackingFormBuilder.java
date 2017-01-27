package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.backingform.DonationTypeBackingForm;

public class DonationTypeBackingFormBuilder extends AbstractBuilder<DonationTypeBackingForm> {

  private Long id;
  private String donationType;

  public DonationTypeBackingFormBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public DonationTypeBackingFormBuilder withDonationType(String donationType) {
    this.donationType = donationType;
    return this;
  }
  
  @Override
  public DonationTypeBackingForm build() {
    DonationTypeBackingForm backingForm = new DonationTypeBackingForm();
    backingForm.setId(id);
    backingForm.setType(donationType);
    return backingForm;
  }
  
  public static DonationTypeBackingFormBuilder aDonationTypeBackingForm() {
    return new DonationTypeBackingFormBuilder();
  }

}

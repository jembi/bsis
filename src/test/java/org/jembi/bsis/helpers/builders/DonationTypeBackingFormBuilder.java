package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.backingform.DonationTypeBackingForm;

public class DonationTypeBackingFormBuilder extends AbstractBuilder<DonationTypeBackingForm> {

  private UUID id;
  private String donationType;
  private boolean isDeleted = false;

  public DonationTypeBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public DonationTypeBackingFormBuilder thatIsDeleted() {
    this.isDeleted = true;
    return this;
  }

  public DonationTypeBackingFormBuilder thatIsNotDeleted() {
    this.isDeleted = false;
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
    backingForm.setIsDeleted(isDeleted);
    return backingForm;
  }
  
  public static DonationTypeBackingFormBuilder aDonationTypeBackingForm() {
    return new DonationTypeBackingFormBuilder();
  }

}

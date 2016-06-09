package helpers.builders;

import backingform.DonationTypeBackingForm;

public class DonationTypeBackingFormBuilder extends AbstractBuilder<DonationTypeBackingForm> {

  private Long id;
  private String type;

  public DonationTypeBackingFormBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public DonationTypeBackingFormBuilder withType(String type) {
    this.type = type;
    return this;
  }

  @Override
  public DonationTypeBackingForm build() {
    DonationTypeBackingForm backingForm = new DonationTypeBackingForm();
    backingForm.setId(id);
    backingForm.setType(type);
    return backingForm;
  }
  
  public static DonationTypeBackingFormBuilder aDonationTypeBackingForm() {
    return new DonationTypeBackingFormBuilder();
  }

}

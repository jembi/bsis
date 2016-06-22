package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.backingform.DonorBackingForm;

public class DonorBackingFormBuilder {

  private Long id;

  public DonorBackingFormBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public DonorBackingForm build() {
    DonorBackingForm backingForm = new DonorBackingForm();
    backingForm.setId(id);
    return backingForm;
  }

  public static DonorBackingFormBuilder aDonorBackingForm() {
    return new DonorBackingFormBuilder();
  }

}

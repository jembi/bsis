package org.jembi.bsis.helpers.builders;

import java.util.UUID;

import org.jembi.bsis.backingform.DonorBackingForm;

public class DonorBackingFormBuilder {

  private UUID id;

  public DonorBackingFormBuilder withId(UUID id) {
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

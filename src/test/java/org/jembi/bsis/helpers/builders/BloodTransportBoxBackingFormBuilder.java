package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.backingform.BloodTransportBoxBackingForm;

public class BloodTransportBoxBackingFormBuilder extends AbstractBuilder<BloodTransportBoxBackingForm> {

  private Long id;
  private double temperature;
  private boolean deleted;

  public BloodTransportBoxBackingFormBuilder withId(Long id) {
    this.id = id;
    return this;
  }
  
  public BloodTransportBoxBackingFormBuilder withTemperature(double temperature) {
    this.temperature = temperature;
    return this;
  }

  public BloodTransportBoxBackingFormBuilder thatIsDeleted() {
    deleted = true;
    return this;
  }

  @Override
  public BloodTransportBoxBackingForm build() {
    BloodTransportBoxBackingForm bloodTransportBox = new BloodTransportBoxBackingForm();
    bloodTransportBox.setId(id);
    bloodTransportBox.setTemperature(temperature);
    bloodTransportBox.setDeleted(deleted);
    return bloodTransportBox;
  }

  public static BloodTransportBoxBackingFormBuilder aBloodTransportBoxBackingForm() {
    return new BloodTransportBoxBackingFormBuilder();
  }
}
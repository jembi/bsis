package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.componentbatch.BloodTransportBox;

public class BloodTransportBoxBuilder extends AbstractEntityBuilder<BloodTransportBox> {

  private Long id;
  private double temperature;
  private boolean deleted;

  public BloodTransportBoxBuilder withId(Long id) {
    this.id = id;
    return this;
  }
  
  public BloodTransportBoxBuilder withTemperature(double temperature) {
    this.temperature = temperature;
    return this;
  }

  public BloodTransportBoxBuilder thatIsDeleted() {
    deleted = true;
    return this;
  }

  @Override
  public BloodTransportBox build() {
    BloodTransportBox bloodTransportBox = new BloodTransportBox();
    bloodTransportBox.setId(id);
    bloodTransportBox.setTemperature(temperature);
    bloodTransportBox.setIsDeleted(deleted);
    return bloodTransportBox;
  }

  public static BloodTransportBoxBuilder aBloodTransportBox() {
    return new BloodTransportBoxBuilder();
  }

}

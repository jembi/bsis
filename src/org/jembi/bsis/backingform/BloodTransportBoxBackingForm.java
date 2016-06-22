package org.jembi.bsis.backingform;

import org.jembi.bsis.model.componentbatch.BloodTransportBox;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BloodTransportBoxBackingForm {

  private Long id;
  private boolean isDeleted = false;
  private Double temperature;

  public BloodTransportBoxBackingForm() {
    super();
  }
  
  @JsonIgnore
  public BloodTransportBox getBloodTransportBox() {
    BloodTransportBox box = new BloodTransportBox();
    box.setId(getId());
    if (getTemperature() != null) { // can't autobox null into a primative
      box.setTemperature(getTemperature());
    }
    return box;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean isDeleted() {
    return isDeleted;
  }

  public void setDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public Double getTemperature() {
    return temperature;
  }

  public void setTemperature(Double temperature) {
    this.temperature = temperature;
  }
}

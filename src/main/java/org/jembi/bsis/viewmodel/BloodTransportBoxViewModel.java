package org.jembi.bsis.viewmodel;

public class BloodTransportBoxViewModel {

  private Long id;
  private double temperature;

  public BloodTransportBoxViewModel() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public double getTemperature() {
    return temperature;
  }

  public void setTemperature(double temperature) {
    this.temperature = temperature;
  }
}

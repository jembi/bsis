package backingform;

public class BloodTransportBoxBackingForm {

  private Long id;
  private boolean isDeleted = false;
  private Double temperature;

  public BloodTransportBoxBackingForm() {
    super();
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

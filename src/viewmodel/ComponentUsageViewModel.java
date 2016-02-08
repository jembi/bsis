package viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import utils.CustomDateFormatter;
import model.usage.ComponentUsage;

public class ComponentUsageViewModel {

  private ComponentUsage componentUsage;

  public ComponentUsageViewModel(ComponentUsage componentUsage) {
    this.componentUsage = componentUsage;
  }

  public Long getId() {
    return componentUsage.getId();
  }

  @JsonIgnore
  public String getDonationIdentificationNumber() {
    return componentUsage.getComponent().getDonationIdentificationNumber();
  }

  public String getHospital() {
    return componentUsage.getHospital();
  }

  public String getWard() {
    return componentUsage.getWard();
  }

  public String getPatientName() {
    return componentUsage.getPatientName();
  }

  public String getUsedBy() {
    return componentUsage.getUsedBy();
  }

  public String getUsageDate() {
    return CustomDateFormatter.getDateTimeString(componentUsage.getUsageDate());
  }

  public String getNotes() {
    return componentUsage.getNotes();
  }

  public String getUseIndication() {
    return componentUsage.getUseIndication();
  }
}

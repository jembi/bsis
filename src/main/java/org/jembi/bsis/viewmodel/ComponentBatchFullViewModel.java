package org.jembi.bsis.viewmodel;

import java.util.List;

public class ComponentBatchFullViewModel extends ComponentBatchViewModel {

  private List<BloodTransportBoxViewModel> bloodTransportBoxes;
  private List<ComponentFullViewModel> components;
  
  public ComponentBatchFullViewModel() {
    super();
  }

  public List<BloodTransportBoxViewModel> getBloodTransportBoxes() {
    return bloodTransportBoxes;
  }

  public void setBloodTransportBoxes(List<BloodTransportBoxViewModel> bloodTransportBoxes) {
    this.bloodTransportBoxes = bloodTransportBoxes;
  }

  public List<ComponentFullViewModel> getComponents() {
    return components;
  }

  public void setComponents(List<ComponentFullViewModel> components) {
    this.components = components;
  }  
}

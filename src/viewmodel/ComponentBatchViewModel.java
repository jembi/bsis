package viewmodel;

import java.util.List;

public class ComponentBatchViewModel extends ComponentBatchBasicViewModel {

  private List<BloodTransportBoxViewModel> bloodTransportBoxes;
  private List<ComponentViewModel> components;
  
  public ComponentBatchViewModel() {
    super();
  }

  public List<BloodTransportBoxViewModel> getBloodTransportBoxes() {
    return bloodTransportBoxes;
  }

  public void setBloodTransportBoxes(List<BloodTransportBoxViewModel> bloodTransportBoxes) {
    this.bloodTransportBoxes = bloodTransportBoxes;
  }

  public List<ComponentViewModel> getComponents() {
    return components;
  }

  public void setComponents(List<ComponentViewModel> components) {
    this.components = components;
  }  
}

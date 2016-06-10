package viewmodel;

import java.util.ArrayList;
import java.util.List;

public class ReturnFormFullViewModel extends ReturnFormViewModel {

  private List<ComponentViewModel> components = new ArrayList<>();

  public List<ComponentViewModel> getComponents() {
    return components;
  }

  public void setComponents(List<ComponentViewModel> components) {
    this.components = components;
  }

}

package viewmodel;

import java.util.ArrayList;
import java.util.List;

public class OrderFormFullViewModel extends OrderFormViewModel {
  
  private List<OrderFormItemViewModel> items = new ArrayList<>();

  private List<ComponentViewModel> components = new ArrayList<>();

  public List<OrderFormItemViewModel> getItems() {
    return items;
  }

  public void setItems(List<OrderFormItemViewModel> items) {
    this.items = items;
  }

  public List<ComponentViewModel> getComponents() {
    return components;
  }

  public void setComponents(List<ComponentViewModel> components) {
    this.components = components;
  }

}
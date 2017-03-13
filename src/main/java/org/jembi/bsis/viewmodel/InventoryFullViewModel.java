package org.jembi.bsis.viewmodel;

import java.util.List;

public class InventoryFullViewModel extends InventoryViewModel {

  private List<OrderFormViewModel> orderForms;

  public List<OrderFormViewModel> getOrderForms() {
    return orderForms;
  }

  public void setOrderForms(List<OrderFormViewModel> orderForms) {
    this.orderForms = orderForms;
  }
}

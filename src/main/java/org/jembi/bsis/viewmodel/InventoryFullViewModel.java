package org.jembi.bsis.viewmodel;

public class InventoryFullViewModel extends InventoryViewModel {

  private OrderFormViewModel orderform;

  public OrderFormViewModel getOrderform() {
    return orderform;
  }

  public void setOrderform(OrderFormViewModel orderform) {
    this.orderform = orderform;
  }
}

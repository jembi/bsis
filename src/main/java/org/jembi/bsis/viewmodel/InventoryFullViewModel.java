package org.jembi.bsis.viewmodel;

public class InventoryFullViewModel extends InventoryViewModel {

  private OrderFormViewModel orderForm;

  public OrderFormViewModel getOrderForm() {
    return orderForm;
  }

  public void setOrderForm(OrderFormViewModel orderForm) {
    this.orderForm = orderForm;
  }
}

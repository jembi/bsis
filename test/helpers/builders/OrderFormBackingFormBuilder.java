package helpers.builders;

import java.util.Date;

import backingform.LocationBackingForm;
import backingform.OrderFormBackingForm;

public class OrderFormBackingFormBuilder {

  private Long id;
  private Date orderDate;
  private LocationBackingForm dispatchedFrom;
  private LocationBackingForm dispatchedTo;

  public OrderFormBackingFormBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public OrderFormBackingFormBuilder withOrderDate(Date orderDate) {
    this.orderDate = orderDate;
    return this;
  }

  public OrderFormBackingFormBuilder withDispatchedFrom(LocationBackingForm dispatchedFrom) {
    this.dispatchedFrom = dispatchedFrom;
    return this;
  }

  public OrderFormBackingFormBuilder withDispatchedTo(LocationBackingForm dispatchedTo) {
    this.dispatchedTo = dispatchedTo;
    return this;
  }

  public OrderFormBackingForm build() {
    OrderFormBackingForm backingForm = new OrderFormBackingForm();
    backingForm.setId(id);
    backingForm.setDispatchedFrom(dispatchedFrom);
    backingForm.setDispatchedTo(dispatchedTo);
    backingForm.setOrderDate(orderDate);
    return backingForm;
  }

  public static OrderFormBackingFormBuilder anOrderFormBackingForm() {
    return new OrderFormBackingFormBuilder();
  }
}

package helpers.builders;

import java.util.Date;

import backingform.LocationBackingForm;
import backingform.OrderFormBackingForm;
import model.order.OrderStatus;
import model.order.OrderType;

public class OrderFormBackingFormBuilder {

  private Long id;
  private Date orderDate;
  private LocationBackingForm dispatchedFrom;
  private LocationBackingForm dispatchedTo;
  private OrderStatus status = OrderStatus.CREATED;
  private OrderType type = OrderType.ISSUE;

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

  public OrderFormBackingFormBuilder withOrderStatus(OrderStatus status) {
    this.status = status;
    return this;
  }

  public OrderFormBackingFormBuilder withOrderType(OrderType type) {
    this.type = type;
    return this;
  }

  public OrderFormBackingForm build() {
    OrderFormBackingForm backingForm = new OrderFormBackingForm();
    backingForm.setId(id);
    backingForm.setDispatchedFrom(dispatchedFrom);
    backingForm.setDispatchedTo(dispatchedTo);
    backingForm.setOrderDate(orderDate);
    backingForm.setStatus(status);
    backingForm.setType(type);
    return backingForm;
  }

  public static OrderFormBackingFormBuilder anOrderFormBackingForm() {
    return new OrderFormBackingFormBuilder();
  }
}

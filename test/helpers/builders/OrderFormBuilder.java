package helpers.builders;

import static helpers.builders.LocationBuilder.aLocation;

import java.util.Date;

import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.OrderFormPersister;
import model.location.Location;
import model.order.OrderForm;
import model.order.OrderStatus;
import model.order.OrderType;

public class OrderFormBuilder extends AbstractEntityBuilder<OrderForm> {
  
  private Long id;
  private Date orderDate = new Date();
  private Location dispatchedFrom = aLocation().build();
  private Location dispatchedTo = aLocation().build();
  private OrderStatus status = OrderStatus.CREATED;
  private OrderType type = OrderType.ISSUE;
  private boolean isDeleted = false;

  public OrderFormBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public OrderFormBuilder withOrderDate(Date orderDate) {
    this.orderDate = orderDate;
    return this;
  }
  
  public OrderFormBuilder withDispatchedFrom(Location dispatchedFrom) {
    this.dispatchedFrom = dispatchedFrom;
    return this;
  }
  
  public OrderFormBuilder withDispatchedTo(Location dispatchedTo) {
    this.dispatchedTo = dispatchedTo;
    return this;
  }

  public OrderFormBuilder withOrderStatus(OrderStatus status) {
    this.status = status;
    return this;
  }

  public OrderFormBuilder withOrderType(OrderType type) {
    this.type = type;
    return this;
  }

  public OrderFormBuilder withIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }

  @Override
  public OrderForm build() {
    OrderForm orderForm = new OrderForm();
    orderForm.setId(id);
    orderForm.setOrderDate(orderDate);
    orderForm.setDispatchedFrom(dispatchedFrom);
    orderForm.setDispatchedTo(dispatchedTo);
    orderForm.setStatus(status);
    orderForm.setType(type);
    orderForm.setIsDeleted(isDeleted);
    return orderForm;
  }

  @Override
  public AbstractEntityPersister<OrderForm> getPersister() {
    return new OrderFormPersister();
  }
  
  public static OrderFormBuilder anOrderForm() {
    return new OrderFormBuilder();
  }

}

package org.jembi.bsis.helpers.builders;

import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.OrderFormPersister;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.order.OrderForm;
import org.jembi.bsis.model.order.OrderFormItem;
import org.jembi.bsis.model.order.OrderStatus;
import org.jembi.bsis.model.order.OrderType;

public class OrderFormBuilder extends AbstractEntityBuilder<OrderForm> {
  
  private Long id;
  private Date orderDate = new Date();
  private Location dispatchedFrom = aLocation().build();
  private Location dispatchedTo = aLocation().build();
  private OrderStatus status = OrderStatus.CREATED;
  private OrderType type = OrderType.ISSUE;
  private boolean isDeleted = false;
  private List<OrderFormItem> items = new ArrayList<>();
  private Date createdDate;
  private List<Component> components = new ArrayList<>();

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

  public OrderFormBuilder withOrderFormItem(OrderFormItem item) {
    items.add(item);
    return this;
  }

  public OrderFormBuilder withOrderFormItems(List<OrderFormItem> items) {
    this.items = items;
    return this;
  }
  
  public OrderFormBuilder withCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }
  
  public OrderFormBuilder withComponents(List<Component> components) {
    this.components = components;
    return this;
  }
  
  public OrderFormBuilder withComponent(Component component) {
    this.components.add(component);
    return this;
  }

  @Override
  public OrderForm build() {
    OrderForm orderForm = new OrderForm();
    orderForm.setId(id);
    orderForm.setCreatedDate(createdDate);
    orderForm.setOrderDate(orderDate);
    orderForm.setDispatchedFrom(dispatchedFrom);
    orderForm.setDispatchedTo(dispatchedTo);
    orderForm.setStatus(status);
    orderForm.setType(type);
    orderForm.setIsDeleted(isDeleted);
    orderForm.setItems(items);
    orderForm.setComponents(components);
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

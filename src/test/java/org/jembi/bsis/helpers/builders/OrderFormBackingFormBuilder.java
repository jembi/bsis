package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.OrderFormBackingForm;
import org.jembi.bsis.backingform.OrderFormItemBackingForm;
import org.jembi.bsis.backingform.PatientBackingForm;
import org.jembi.bsis.model.order.OrderStatus;
import org.jembi.bsis.model.order.OrderType;

public class OrderFormBackingFormBuilder {

  private UUID id;
  private Date orderDate;
  private LocationBackingForm dispatchedFrom;
  private LocationBackingForm dispatchedTo;
  private OrderStatus status = OrderStatus.CREATED;
  private OrderType type = OrderType.ISSUE;
  private List<OrderFormItemBackingForm> items;
  private List<ComponentBackingForm> components = new ArrayList<>();
  private PatientBackingForm patient;

  public OrderFormBackingFormBuilder withId(UUID id) {
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
  
  public OrderFormBackingFormBuilder withPatient(PatientBackingForm patient) {
    this.patient = patient;
    return this;
  }

  public OrderFormBackingFormBuilder withItem(OrderFormItemBackingForm item) {
    if (items == null) {
      items = new ArrayList<>();
    }
    items.add(item);
    return this;
  }

  public OrderFormBackingFormBuilder withComponent(ComponentBackingForm component) {
    components.add(component);
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
    backingForm.setItems(items);
    backingForm.setComponents(components);
    backingForm.setPatient(patient);
    return backingForm;
  }

  public static OrderFormBackingFormBuilder anOrderFormBackingForm() {
    return new OrderFormBackingFormBuilder();
  }
}

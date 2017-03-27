package org.jembi.bsis.backingform;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.model.order.OrderStatus;
import org.jembi.bsis.model.order.OrderType;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class OrderFormBackingForm {

  private UUID id;

  private Date orderDate;

  private LocationBackingForm dispatchedFrom;

  private LocationBackingForm dispatchedTo;

  private PatientBackingForm patient;

  private OrderStatus status;

  private OrderType type;
  
  private List<OrderFormItemBackingForm> items;

  private List<ComponentBackingForm> components;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Date getOrderDate() {
    return orderDate;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public LocationBackingForm getDispatchedFrom() {
    return dispatchedFrom;
  }

  public void setDispatchedFrom(LocationBackingForm dispatchedFrom) {
    this.dispatchedFrom = dispatchedFrom;
  }

  public LocationBackingForm getDispatchedTo() {
    return dispatchedTo;
  }

  public void setDispatchedTo(LocationBackingForm dispatchedTo) {
    this.dispatchedTo = dispatchedTo;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  public OrderType getType() {
    return type;
  }

  public void setType(OrderType type) {
    this.type = type;
  }

  public List<OrderFormItemBackingForm> getItems() {
    return items;
  }

  public void setItems(List<OrderFormItemBackingForm> items) {
    this.items = items;
  }

  public List<ComponentBackingForm> getComponents() {
    return components;
  }

  public void setComponents(List<ComponentBackingForm> components) {
    this.components = components;
  }

  public PatientBackingForm getPatient () {
    return patient;
  }

  public void setPatient (PatientBackingForm patient) {
    this.patient = patient;
  }

  @JsonIgnore
  public void setPermissions(Map<String, Boolean> permissions) {
    // ignore
  }

}

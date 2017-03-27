package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.order.OrderStatus;
import org.jembi.bsis.model.order.OrderType;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class OrderFormViewModel extends BaseViewModel<UUID> {

  private Date orderDate;

  private LocationFullViewModel dispatchedFrom;

  private LocationFullViewModel dispatchedTo;

  private PatientViewModel patient;

  private OrderStatus status;

  private OrderType type;

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public LocationFullViewModel getDispatchedFrom() {
    return dispatchedFrom;
  }

  public void setDispatchedFrom(LocationFullViewModel dispatchedFrom) {
    this.dispatchedFrom = dispatchedFrom;
  }

  public LocationFullViewModel getDispatchedTo() {
    return dispatchedTo;
  }

  public void setDispatchedTo(LocationFullViewModel dispatchedTo) {
    this.dispatchedTo = dispatchedTo;
  }

  public PatientViewModel getPatient () {
    return patient;
  }

  public void setPatient (PatientViewModel patient) {
    this.patient = patient;
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
}

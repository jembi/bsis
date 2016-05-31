package viewmodel;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import model.order.OrderStatus;
import model.order.OrderType;
import utils.DateTimeSerialiser;

public class OrderFormViewModel extends BaseViewModel {

  private Date orderDate;

  private LocationViewModel dispatchedFrom;

  private LocationViewModel dispatchedTo;

  private OrderStatus status;

  private OrderType type;

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getOrderDate() {
    return orderDate;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public void setOrderDate(Date orderDate) {
    this.orderDate = orderDate;
  }

  public LocationViewModel getDispatchedFrom() {
    return dispatchedFrom;
  }

  public void setDispatchedFrom(LocationViewModel dispatchedFrom) {
    this.dispatchedFrom = dispatchedFrom;
  }

  public LocationViewModel getDispatchedTo() {
    return dispatchedTo;
  }

  public void setDispatchedTo(LocationViewModel dispatchedTo) {
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
}

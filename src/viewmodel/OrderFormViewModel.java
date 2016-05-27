package viewmodel;

import java.util.Date;

import model.order.OrderStatus;
import model.order.OrderType;

public class OrderFormViewModel extends BaseViewModel {

  private Date orderDate;

  private LocationViewModel dispatchedFrom;

  private LocationViewModel dispatchedTo;

  private OrderStatus status;

  private OrderType type;

  public Date getOrderDate() {
    return orderDate;
  }

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

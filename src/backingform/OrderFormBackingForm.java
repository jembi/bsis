package backingform;

import java.util.Date;

import model.order.OrderStatus;
import model.order.OrderType;

public class OrderFormBackingForm {

  private Long id;

  private Date orderDate;

  private LocationBackingForm dispatchedFrom;

  private LocationBackingForm dispatchedTo;

  private OrderStatus status;

  private OrderType type;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getOrderDate() {
    return orderDate;
  }

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



}

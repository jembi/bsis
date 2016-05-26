package viewmodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.order.OrderStatus;
import model.order.OrderType;

public class OrderFormViewModel {

  private long id;

  private Date orderDate;

  private LocationViewModel dispatchedFrom;

  private LocationViewModel dispatchedTo;

  private OrderStatus status;

  private OrderType type;
  
  private List<OrderFormItemViewModel> items = new ArrayList<>();

  private List<ComponentViewModel> components = new ArrayList<>();

  public Date getOrderDate() {
    return orderDate;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public List<OrderFormItemViewModel> getItems() {
    return items;
  }

  public void setItems(List<OrderFormItemViewModel> items) {
    this.items = items;
  }

  public List<ComponentViewModel> getComponents() {
    return components;
  }

  public void setComponents(List<ComponentViewModel> components) {
    this.components = components;
  }


}

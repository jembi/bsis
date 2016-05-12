package viewmodel;

import java.util.Date;

import model.location.Location;

public class OrderFormViewModel {

  private long id;

  private Date orderDate;

  private Location dispatchedFrom;

  private Location dispatchedTo;

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

  public Location getDispatchedFrom() {
    return dispatchedFrom;
  }

  public void setDispatchedFrom(Location dispatchedFrom) {
    this.dispatchedFrom = dispatchedFrom;
  }

  public Location getDispatchedTo() {
    return dispatchedTo;
  }

  public void setDispatchedTo(Location dispatchedTo) {
    this.dispatchedTo = dispatchedTo;
  }

}

package viewmodel;

import java.util.Date;

public class OrderFormViewModel {

  private long id;

  private Date orderDate;

  private LocationViewModel dispatchedFrom;

  private LocationViewModel dispatchedTo;

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

}

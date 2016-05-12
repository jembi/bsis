package backingform;

import java.util.Date;

import model.location.Location;

public class OrderFormBackingForm {

  private Long id;

  private Date orderDate;

  private Location dispatchedFrom;

  private Location dispatchedTo;

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

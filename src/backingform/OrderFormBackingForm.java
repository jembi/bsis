package backingform;

import java.util.Date;

public class OrderFormBackingForm {

  private Long id;

  private Date orderDate;

  private LocationBackingForm dispatchedFrom;

  private LocationBackingForm dispatchedTo;

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


}

package model.order;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import model.BaseModificationTrackerEntity;
import model.location.Location;

@Entity
@Audited
public class OrderForm extends BaseModificationTrackerEntity {
  
  private static final long serialVersionUID = 1L;

  @Column(nullable = false)
  private Date orderDate;
  
  @ManyToOne(optional = false)
  private Location dispatchedFrom;
  
  @ManyToOne(optional = false)
  private Location dispatchedTo;
  
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

package model.returnform;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import model.BaseModificationTrackerEntity;
import model.location.Location;

@Entity
@Audited
public class ReturnForm extends BaseModificationTrackerEntity {
  
  private static final long serialVersionUID = 1L;

  @Column(nullable = false)
  private Date returnDate;
  
  @ManyToOne(optional = false)
  private Location returnedFrom;
  
  @ManyToOne(optional = false)
  private Location returnedTo;
  
  @Enumerated(EnumType.STRING)
  @Column(length = 20, nullable = false)
  private ReturnStatus status = ReturnStatus.CREATED;

  @Column
  private boolean isDeleted = false;

  public Date getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(Date returnDate) {
    this.returnDate = returnDate;
  }

  public Location getReturnedFrom() {
    return returnedFrom;
  }

  public void setReturnedFrom(Location returnedFrom) {
    this.returnedFrom = returnedFrom;
  }

  public Location getReturnedTo() {
    return returnedTo;
  }

  public void setReturnedTo(Location returnedTo) {
    this.returnedTo = returnedTo;
  }

  public ReturnStatus getStatus() {
    return status;
  }

  public void setStatus(ReturnStatus status) {
    this.status = status;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

}

package backingform;

import java.util.Date;

import model.returnform.ReturnStatus;

public class ReturnFormBackingForm {

  private Long id;

  private Date returnDate;

  private LocationBackingForm returnedFrom;

  private LocationBackingForm returnedTo;

  private ReturnStatus status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(Date returnDate) {
    this.returnDate = returnDate;
  }

  public LocationBackingForm getReturnedFrom() {
    return returnedFrom;
  }

  public void setReturnedFrom(LocationBackingForm returnedFrom) {
    this.returnedFrom = returnedFrom;
  }

  public LocationBackingForm getReturnedTo() {
    return returnedTo;
  }

  public void setReturnedTo(LocationBackingForm returnedTo) {
    this.returnedTo = returnedTo;
  }

  public ReturnStatus getStatus() {
    return status;
  }

  public void setStatus(ReturnStatus status) {
    this.status = status;
  }

}

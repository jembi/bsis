package backingform;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import model.returnform.ReturnStatus;
import utils.DateTimeSerialiser;

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

  @JsonSerialize(using = DateTimeSerialiser.class)
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
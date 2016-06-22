package org.jembi.bsis.viewmodel;

import java.util.Date;

import org.jembi.bsis.model.returnform.ReturnStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ReturnFormViewModel extends BaseViewModel {

  private Date returnDate;

  private LocationViewModel returnedFrom;

  private LocationViewModel returnedTo;

  private ReturnStatus status;

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(Date orderDate) {
    this.returnDate = orderDate;
  }

  public LocationViewModel getReturnedFrom() {
    return returnedFrom;
  }

  public void setReturnedFrom(LocationViewModel returnedFrom) {
    this.returnedFrom = returnedFrom;
  }

  public LocationViewModel getReturnedTo() {
    return returnedTo;
  }

  public void setReturnedTo(LocationViewModel returnedTo) {
    this.returnedTo = returnedTo;
  }

  public ReturnStatus getStatus() {
    return status;
  }

  public void setStatus(ReturnStatus status) {
    this.status = status;
  }

}

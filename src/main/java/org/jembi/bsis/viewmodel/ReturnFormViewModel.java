package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.returnform.ReturnStatus;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ReturnFormViewModel extends BaseViewModel<UUID> {

  private Date returnDate;

  private LocationFullViewModel returnedFrom;

  private LocationFullViewModel returnedTo;

  private ReturnStatus status;

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getReturnDate() {
    return returnDate;
  }

  public void setReturnDate(Date orderDate) {
    this.returnDate = orderDate;
  }

  public LocationFullViewModel getReturnedFrom() {
    return returnedFrom;
  }

  public void setReturnedFrom(LocationFullViewModel returnedFrom) {
    this.returnedFrom = returnedFrom;
  }

  public LocationFullViewModel getReturnedTo() {
    return returnedTo;
  }

  public void setReturnedTo(LocationFullViewModel returnedTo) {
    this.returnedTo = returnedTo;
  }

  public ReturnStatus getStatus() {
    return status;
  }

  public void setStatus(ReturnStatus status) {
    this.status = status;
  }

}
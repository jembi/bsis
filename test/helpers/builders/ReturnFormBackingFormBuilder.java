package helpers.builders;

import java.util.Date;

import backingform.LocationBackingForm;
import backingform.ReturnFormBackingForm;
import model.returnform.ReturnStatus;

public class ReturnFormBackingFormBuilder {

  private Long id;
  private Date returnDate;
  private LocationBackingForm returnedFrom;
  private LocationBackingForm returnedTo;
  private ReturnStatus status = ReturnStatus.CREATED;

  public ReturnFormBackingFormBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public ReturnFormBackingFormBuilder withReturnDate(Date returnDate) {
    this.returnDate = returnDate;
    return this;
  }

  public ReturnFormBackingFormBuilder withReturnedFrom(LocationBackingForm returnedFrom) {
    this.returnedFrom = returnedFrom;
    return this;
  }

  public ReturnFormBackingFormBuilder withReturnedTo(LocationBackingForm returnedTo) {
    this.returnedTo = returnedTo;
    return this;
  }

  public ReturnFormBackingFormBuilder withReturnStatus(ReturnStatus status) {
    this.status = status;
    return this;
  }

  public ReturnFormBackingForm build() {
    ReturnFormBackingForm backingForm = new ReturnFormBackingForm();
    backingForm.setId(id);
    backingForm.setReturnedFrom(returnedFrom);
    backingForm.setReturnedTo(returnedTo);
    backingForm.setReturnDate(returnDate);
    backingForm.setStatus(status);
    return backingForm;
  }

  public static ReturnFormBackingFormBuilder aReturnFormBackingForm() {
    return new ReturnFormBackingFormBuilder();
  }
}

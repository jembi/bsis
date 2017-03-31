package org.jembi.bsis.helpers.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.backingform.ComponentBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.ReturnFormBackingForm;
import org.jembi.bsis.model.returnform.ReturnStatus;

public class ReturnFormBackingFormBuilder extends AbstractBuilder<ReturnFormBackingForm> {

  private UUID id;
  private Date returnDate;
  private LocationBackingForm returnedFrom;
  private LocationBackingForm returnedTo;
  private ReturnStatus status = ReturnStatus.CREATED;
  private List<ComponentBackingForm> components = new ArrayList<>();

  public ReturnFormBackingFormBuilder withId(UUID id) {
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

  public ReturnFormBackingFormBuilder withComponent(ComponentBackingForm component) {
    components.add(component);
    return this;
  }

  @Override
  public ReturnFormBackingForm build() {
    ReturnFormBackingForm backingForm = new ReturnFormBackingForm();
    backingForm.setId(id);
    backingForm.setReturnedFrom(returnedFrom);
    backingForm.setReturnedTo(returnedTo);
    backingForm.setReturnDate(returnDate);
    backingForm.setStatus(status);
    backingForm.setComponents(components);
    return backingForm;
  }

  public static ReturnFormBackingFormBuilder aReturnFormBackingForm() {
    return new ReturnFormBackingFormBuilder();
  }
}
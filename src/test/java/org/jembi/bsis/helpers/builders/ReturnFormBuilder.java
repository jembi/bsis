package org.jembi.bsis.helpers.builders;

import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.ReturnFormPersister;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.returnform.ReturnForm;
import org.jembi.bsis.model.returnform.ReturnStatus;
import org.jembi.bsis.util.RandomTestDate;

public class ReturnFormBuilder extends AbstractEntityBuilder<ReturnForm> {

  private UUID id;
  private boolean isDeleted = false;
  private Date returnDate = new RandomTestDate();
  private Location returnedFrom = aLocation().build();
  private Location returnedTo = aLocation().build();
  private ReturnStatus status = ReturnStatus.CREATED;
  private List<Component> components = new ArrayList<>();

  public ReturnFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public ReturnFormBuilder withReturnDate(Date returnDate) {
    this.returnDate = returnDate;
    return this;
  }

  public ReturnFormBuilder withReturnedFrom(Location returnedFrom) {
    this.returnedFrom = returnedFrom;
    return this;
  }

  public ReturnFormBuilder withReturnedTo(Location returnedTo) {
    this.returnedTo = returnedTo;
    return this;
  }

  public ReturnFormBuilder withReturnStatus(ReturnStatus status) {
    this.status = status;
    return this;
  }

  public ReturnFormBuilder withIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }

  public ReturnFormBuilder withComponents(List<Component> components) {
    this.components = components;
    return this;
  }

  public ReturnFormBuilder withComponent(Component component) {
    components.add(component);
    return this;
  }

  @Override
  public ReturnForm build() {
    ReturnForm returnForm = new ReturnForm();
    returnForm.setId(id);
    returnForm.setIsDeleted(isDeleted);
    returnForm.setReturnDate(returnDate);
    returnForm.setReturnedFrom(returnedFrom);
    returnForm.setReturnedTo(returnedTo);
    returnForm.setStatus(status);
    returnForm.setComponents(components);
    return returnForm;
  }

  @Override
  public AbstractEntityPersister<ReturnForm> getPersister() {
    return new ReturnFormPersister();
  }

  public static ReturnFormBuilder aReturnForm() {
    return new ReturnFormBuilder();
  }

}
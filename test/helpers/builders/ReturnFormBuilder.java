package helpers.builders;

import static helpers.builders.LocationBuilder.aLocation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import helpers.persisters.AbstractEntityPersister;
import helpers.persisters.ReturnFormPersister;
import model.component.Component;
import model.location.Location;
import model.returnform.ReturnForm;
import model.returnform.ReturnStatus;

public class ReturnFormBuilder extends AbstractEntityBuilder<ReturnForm> {

  private Long id;
  private boolean isDeleted = false;
  private Date returnDate = new Date();
  private Location returnedFrom = aLocation().build();
  private Location returnedTo = aLocation().build();
  private ReturnStatus status = ReturnStatus.CREATED;
  private List<Component> components = new ArrayList<>();

  public ReturnFormBuilder withId(Long id) {
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

package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.patient.Patient;
import org.jembi.bsis.model.transfusion.Transfusion;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.model.transfusion.TransfusionReactionType;
import org.jembi.bsis.model.user.User;

public class TransfusionBuilder extends AbstractEntityBuilder<Transfusion> {

  private UUID id;
  private Date createdDate;
  private User createdBy;
  private Date lastUpdated;
  private User lastUpdatedBy;
  private Patient patient;
  private Component component;
  private Location receivedFrom;
  private TransfusionReactionType transfusionReactionType;
  private TransfusionOutcome transfusionOutcome;
  private Date dateTransfused;
  private String notes;
  private boolean isDeleted;
  
  public TransfusionBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public TransfusionBuilder withPatient(Patient patient) {
    this.patient = patient;
    return this;
  }

  public TransfusionBuilder withComponent(Component component) {
    this.component = component;
    return this;
  }
  
  public TransfusionBuilder withReceivedFrom(Location receivedFrom) {
    this.receivedFrom = receivedFrom;
    return this;
  }
  
  public TransfusionBuilder withTransfusionReactionType(TransfusionReactionType transfusionReactionType) {
    this.transfusionReactionType = transfusionReactionType;
    return this;
  }
  
  public TransfusionBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public TransfusionBuilder thatIsDeleted() {
    isDeleted = true;
    return this;
  }

  public TransfusionBuilder thatIsNotDeleted() {
    isDeleted = false;
    return this;
  }

  public TransfusionBuilder withCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }

  public TransfusionBuilder withCreatedBy(User createdBy) {
    this.createdBy = createdBy;
    return this;
  }

  public TransfusionBuilder withLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  public TransfusionBuilder withLastUpdatedBy(User lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
    return this;
  }

  public TransfusionBuilder withTransfusionOutcome(TransfusionOutcome transfusionOutcome) {
    this.transfusionOutcome = transfusionOutcome;
    return this;
  }

  public TransfusionBuilder withDateTransfused(Date dateTransfused) {
    this.dateTransfused = dateTransfused;
    return this;
  }  

  @Override
  public Transfusion build() {
    Transfusion transfusion = new Transfusion();
    transfusion.setId(id);
    transfusion.setPatient(patient);
    transfusion.setComponent(component);
    transfusion.setReceivedFrom(receivedFrom);
    transfusion.setTransfusionReactionType(transfusionReactionType);
    transfusion.setTransfusionOutcome(transfusionOutcome);
    transfusion.setDateTransfused(dateTransfused);
    transfusion.setNotes(notes);
    transfusion.setIsDeleted(isDeleted);    
    transfusion.setCreatedDate(createdDate);
    transfusion.setCreatedBy(createdBy);
    transfusion.setLastUpdated(lastUpdated);
    transfusion.setLastUpdatedBy(lastUpdatedBy);
    return transfusion;
  }

  public static TransfusionBuilder aTransfusion() {
    return new TransfusionBuilder();
  }
  
}
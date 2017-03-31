package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.backingform.ComponentTypeBackingForm;
import org.jembi.bsis.backingform.LocationBackingForm;
import org.jembi.bsis.backingform.PatientBackingForm;
import org.jembi.bsis.backingform.TransfusionBackingForm;
import org.jembi.bsis.backingform.TransfusionReactionTypeBackingForm;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;

public class TransfusionBackingFormBuilder extends AbstractBuilder<TransfusionBackingForm> {

  private UUID id;
  private String donationIdentificationNumber;
  private PatientBackingForm patient;
  private String componentCode;
  private ComponentTypeBackingForm componentType;
  private LocationBackingForm receivedFrom;
  private TransfusionReactionTypeBackingForm transfusionReactionType;
  private TransfusionOutcome transfusionOutcome;
  private Date dateTransfused;
  private String notes;
  
  public TransfusionBackingFormBuilder withId(UUID id) {
    this.id = id;
    return this;
  }

  public TransfusionBackingFormBuilder withPatient(PatientBackingForm patient) {
    this.patient = patient;
    return this;
  }

  public TransfusionBackingFormBuilder withDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }
  
  public TransfusionBackingFormBuilder withComponentCode(String componentCode) {
    this.componentCode = componentCode;
    return this;
  }
  
  public TransfusionBackingFormBuilder withComponentType(ComponentTypeBackingForm componentType) {
    this.componentType = componentType;
    return this;
  }
  
  public TransfusionBackingFormBuilder withReceivedFrom(LocationBackingForm receivedFrom) {
    this.receivedFrom = receivedFrom;
    return this;
  }
  
  public TransfusionBackingFormBuilder withTransfusionReactionType(TransfusionReactionTypeBackingForm transfusionReactionType) {
    this.transfusionReactionType = transfusionReactionType;
    return this;
  }

  public TransfusionBackingFormBuilder withNotes(String notes) {
    this.notes = notes;
    return this;
  }

  public TransfusionBackingFormBuilder withTransfusionOutcome(TransfusionOutcome transfusionOutcome) {
    this.transfusionOutcome = transfusionOutcome;
    return this;
  }

  public TransfusionBackingFormBuilder withDateTransfused(Date dateTransfused) {
    this.dateTransfused = dateTransfused;
    return this;
  }  

  @Override
  public TransfusionBackingForm build() {
    TransfusionBackingForm transfusionBackingForm = new TransfusionBackingForm();
    transfusionBackingForm.setId(id);
    transfusionBackingForm.setDonationIdentificationNumber(donationIdentificationNumber);
    transfusionBackingForm.setPatient(patient);
    transfusionBackingForm.setComponentCode(componentCode);
    transfusionBackingForm.setComponentType(componentType);
    transfusionBackingForm.setReceivedFrom(receivedFrom);
    transfusionBackingForm.setTransfusionReactionType(transfusionReactionType);
    transfusionBackingForm.setTransfusionOutcome(transfusionOutcome);
    transfusionBackingForm.setDateTransfused(dateTransfused);
    transfusionBackingForm.setNotes(notes);
    return transfusionBackingForm;
  }

  public static TransfusionBackingFormBuilder aTransfusionBackingForm() {
    return new TransfusionBackingFormBuilder();
  }
  
}
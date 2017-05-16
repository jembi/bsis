package org.jembi.bsis.backingform;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TransfusionBackingForm {

  private UUID id;

  private String donationIdentificationNumber;
  
  private PatientBackingForm patient;
  
  private String componentCode;
  
  private ComponentTypeBackingForm componentType;
  
  private LocationBackingForm receivedFrom;
  
  private TransfusionReactionTypeBackingForm transfusionReactionType;
  
  private TransfusionOutcome transfusionOutcome;

  private String notes;
  
  private Date dateTransfused;
  
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public PatientBackingForm getPatient() {
    return patient;
  }

  public void setPatient(PatientBackingForm patient) {
    this.patient = patient;
  }

  public String getComponentCode() {
    return componentCode;
  }

  public void setComponentCode(String componentCode) {
    this.componentCode = componentCode;
  }

  public ComponentTypeBackingForm getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentTypeBackingForm componentType) {
    this.componentType = componentType;
  }

  public LocationBackingForm getReceivedFrom() {
    return receivedFrom;
  }

  public void setReceivedFrom(LocationBackingForm receivedFrom) {
    this.receivedFrom = receivedFrom;
  }

  public TransfusionReactionTypeBackingForm getTransfusionReactionType() {
    return transfusionReactionType;
  }

  public void setTransfusionReactionType(TransfusionReactionTypeBackingForm transfusionReactionType) {
    this.transfusionReactionType = transfusionReactionType;
  }

  public TransfusionOutcome getTransfusionOutcome() {
    return transfusionOutcome;
  }

  public void setTransfusionOutcome(TransfusionOutcome transfusionOutcome) {
    this.transfusionOutcome = transfusionOutcome;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Date getDateTransfused() {
    return dateTransfused;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public void setDateTransfused(Date dateTransfused) {
    this.dateTransfused = dateTransfused;
  }

  @JsonIgnore
  public void setComponent(ComponentBackingForm component) {
    //Ignore
  }
}

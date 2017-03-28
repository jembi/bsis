package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.UUID;

import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TransfusionFullViewModel extends BaseViewModel<UUID> {

  private String donationIdentificationNumber;

  private PatientViewModel patient;

  private ComponentViewModel component;

  private LocationViewModel receivedFrom;

  private TransfusionReactionTypeViewModel transfusionReactionType;

  private TransfusionOutcome transfusionOutcome;

  private Date dateTransfused;

  private String notes;

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public PatientViewModel getPatient() {
    return patient;
  }

  public void setPatient(PatientViewModel patient) {
    this.patient = patient;
  }

  public ComponentViewModel getComponent() {
    return component;
  }

  public void setComponent(ComponentViewModel component) {
    this.component = component;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public LocationViewModel getReceivedFrom() {
    return receivedFrom;
  }

  public void setReceivedFrom(LocationViewModel receivedFrom) {
    this.receivedFrom = receivedFrom;
  }

  public TransfusionReactionTypeViewModel getTransfusionReactionType() {
    return transfusionReactionType;
  }

  public void setTransfusionReactionType(TransfusionReactionTypeViewModel transfusionReactionType) {
    this.transfusionReactionType = transfusionReactionType;
  }

  public TransfusionOutcome getTransfusionOutcome() {
    return transfusionOutcome;
  }

  public void setTransfusionOutcome(TransfusionOutcome transfusionOutcome) {
    this.transfusionOutcome = transfusionOutcome;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getDateTransfused() {
    return dateTransfused;
  }

  public void setDateTransfused(Date dateTransfused) {
    this.dateTransfused = dateTransfused;
  }
}

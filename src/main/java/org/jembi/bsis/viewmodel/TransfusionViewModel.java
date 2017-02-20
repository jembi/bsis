package org.jembi.bsis.viewmodel;

import java.util.Date;

import org.jembi.bsis.model.transfusion.TransfusionOutcome;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TransfusionViewModel {

  private String donationIdentificationNumber;

  private PatientViewModel patient;

  private String componentCode;

  private ComponentTypeViewModel componentType;

  private LocationViewModel usageSite;

  private TransfusionReactionTypeViewModel transfusionReactionType;

  private TransfusionOutcome transfusionOutcome;

  private Date dateTransfused;

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

  public String getComponentCode() {
    return componentCode;
  }

  public void setComponentCode(String componentCode) {
    this.componentCode = componentCode;
  }

  public ComponentTypeViewModel getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentTypeViewModel componentType) {
    this.componentType = componentType;
  }

  public LocationViewModel getUsageSite() {
    return usageSite;
  }

  public void setUsageSite(LocationViewModel usageSite) {
    this.usageSite = usageSite;
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

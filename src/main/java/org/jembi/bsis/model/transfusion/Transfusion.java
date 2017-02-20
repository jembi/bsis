package org.jembi.bsis.model.transfusion;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;
import org.jembi.bsis.model.BaseModificationTrackerEntity;
import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.patient.Patient;

@Entity
@Audited
public class Transfusion extends BaseModificationTrackerEntity {
  
  private static final long serialVersionUID = 1L;
  
  @NotBlank
  private String donationIdentificationNumber;
  
  @ManyToOne(optional = false)
  private Patient patient;
  
  @ManyToOne(optional = false)
  private ComponentType componentType;
  
  @ManyToOne(optional = false)
  private Location receivedFrom;
  
  @ManyToOne(optional = true)
  private TransfusionReactionType transfusionReactionType;
  
  @Enumerated(EnumType.STRING)
  @Column(length = 30)
  private TransfusionOutcome transfusionOutcome;
  
  @Temporal(TemporalType.DATE)
  private Date dateTransfused;
  
  private String notes;
  
  private boolean isDeleted = false;

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public ComponentType getComponentType() {
    return componentType;
  }

  public void setComponentType(ComponentType componentType) {
    this.componentType = componentType;
  }

  public Location getReceivedFrom() {
    return receivedFrom;
  }

  public void setReceivedFrom(Location receivedFrom) {
    this.receivedFrom = receivedFrom;
  }

  public TransfusionReactionType getTransfusionReactionType() {
    return transfusionReactionType;
  }

  public void setTransfusionReactionType(TransfusionReactionType transfusionReactionType) {
    this.transfusionReactionType = transfusionReactionType;
  }

  public TransfusionOutcome getTransfusionOutcome() {
    return transfusionOutcome;
  }

  public void setTransfusionOutcome(TransfusionOutcome transfusionOutcome) {
    this.transfusionOutcome = transfusionOutcome;
  }

  public Date getDateTransfused() {
    return dateTransfused;
  }

  public void setDateTransfused(Date dateTransfused) {
    this.dateTransfused = dateTransfused;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
  }
  
  
}
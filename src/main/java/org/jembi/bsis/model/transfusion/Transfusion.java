package org.jembi.bsis.model.transfusion;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseModificationTrackerUUIDEntity;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.patient.Patient;
import org.jembi.bsis.repository.constant.TransfusionNamedQueryConstants;

@NamedQueries({
  @NamedQuery(name = TransfusionNamedQueryConstants.NAME_FIND_TRANSFUSION_BY_DIN_AND_COMPONENT_CODE,
      query = TransfusionNamedQueryConstants.QUERY_FIND_TRANSFUSION_BY_DIN_AND_COMPONENT_CODE),
  @NamedQuery(name = TransfusionNamedQueryConstants.NAME_FIND_TRANSFUSIONS,
      query = TransfusionNamedQueryConstants.QUERY_FIND_TRANSFUSIONS),
  @NamedQuery(name = TransfusionNamedQueryConstants.NAME_FIND_TRANSFUSION_SUMMARY_RECORDED_FOR_USAGE_SITE_FOR_PERIOD,
      query = TransfusionNamedQueryConstants.QUERY_FIND_TRANSFUSION_SUMMARY_RECORDED_FOR_USAGE_SITE_FOR_PERIOD),
  @NamedQuery(name = TransfusionNamedQueryConstants.NAME_FIND_TRANSFUSION_BY_ID,
      query = TransfusionNamedQueryConstants.QUERY_FIND_TRANSFUSION_BY_ID)
})
@Entity
@Audited
public class Transfusion extends BaseModificationTrackerUUIDEntity {
  
  private static final long serialVersionUID = 1L;
  
  @ManyToOne(optional = false, cascade = CascadeType.ALL)
  private Patient patient;
  
  @ManyToOne(optional = false)
  private Component component;
  
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

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public Component getComponent() {
    return component;
  }

  public void setComponent(Component component) {
    this.component = component;
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
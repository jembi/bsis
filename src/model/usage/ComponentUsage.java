package model.usage;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import model.BaseEntity;
import model.component.Component;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.user.User;

import org.hibernate.envers.Audited;

import constraintvalidator.ComponentExists;

/**
 * Entity to represent the details of how/where/when a Component from a Donation was used. 
 */
@Entity
@Audited
public class ComponentUsage extends BaseEntity implements ModificationTracker {

  private static final long serialVersionUID = 1L;

  @ComponentExists
  @OneToOne
  private Component component;

  @Column(length=50)
  private String hospital;

  @Column(length=50)
  private String patientName;

  @Column(length=30)
  private String ward;

  @Column(length=50)
  private String useIndication;

  @Temporal(TemporalType.TIMESTAMP)
  private Date usageDate;

  @Lob
  private String notes;

  @Valid
  private RowModificationTracker modificationTracker;

  private Boolean isDeleted;

  @Column(length=30)
  private String usedBy;

  public ComponentUsage() {
    modificationTracker = new RowModificationTracker();
  }

  @Override
  public Date getLastUpdated() {
    return modificationTracker.getLastUpdated();
  }

  @Override
  public Date getCreatedDate() {
    return modificationTracker.getCreatedDate();
  }

  @Override
  public User getCreatedBy() {
    return modificationTracker.getCreatedBy();
  }

  @Override
  public User getLastUpdatedBy() {
    return modificationTracker.getLastUpdatedBy();
  }

  @Override
  public void setLastUpdated(Date lastUpdated) {
    modificationTracker.setLastUpdated(lastUpdated);
  }

  @Override
  public void setCreatedDate(Date createdDate) {
    modificationTracker.setCreatedDate(createdDate);
  }

  @Override
  public void setCreatedBy(User createdBy) {
    modificationTracker.setCreatedBy(createdBy);
  }

  @Override
  public void setLastUpdatedBy(User lastUpdatedBy) {
    modificationTracker.setLastUpdatedBy(lastUpdatedBy);
  }

  public String getHospital() {
    return hospital;
  }

  public String getPatientName() {
    return patientName;
  }

  public String getWard() {
    return ward;
  }

  public String getUseIndication() {
    return useIndication;
  }

  public Date getUsageDate() {
    return usageDate;
  }

  public String getNotes() {
    return notes;
  }

  public Component getComponent() {
    return component;
  }

  public RowModificationTracker getModificationTracker() {
    return modificationTracker;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setHospital(String hospital) {
    this.hospital = hospital;
  }

  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public void setWard(String ward) {
    this.ward = ward;
  }

  public void setUseIndication(String useIndication) {
    this.useIndication = useIndication;
  }

  public void setUsageDate(Date usageDate) {
    this.usageDate = usageDate;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setComponent(Component component) {
    this.component = component;
  }

  public void setModificationTracker(RowModificationTracker modificationTracker) {
    this.modificationTracker = modificationTracker;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public String getUsedBy() {
    return usedBy;
  }

  public void setUsedBy(String usedBy) {
    this.usedBy = usedBy;
  }

  public String getDonationIdentificationNumber() {
    if (component == null || component.getDonation() == null || component.getDonationIdentificationNumber() == null)
      return "";
    return component.getDonationIdentificationNumber();
  }
}
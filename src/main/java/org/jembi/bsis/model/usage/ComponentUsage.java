package org.jembi.bsis.model.usage;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.jembi.bsis.model.BaseModificationTrackerEntity;
import org.jembi.bsis.model.component.Component;

/**
 * Entity to represent the details of how/where/when a Component from a Donation was used.
 */
@Entity
@Audited
public class ComponentUsage extends BaseModificationTrackerEntity {

  private static final long serialVersionUID = 1L;

  @OneToOne
  private Component component;

  @Column(length = 50)
  private String hospital;

  @Column(length = 50)
  private String patientName;

  @Column(length = 30)
  private String ward;

  @Column(length = 50)
  private String useIndication;

  @Temporal(TemporalType.TIMESTAMP)
  private Date usageDate;

  @Lob
  private String notes;

  private Boolean isDeleted;

  @Column(length = 30)
  private String usedBy;

  public ComponentUsage() {
    super();
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
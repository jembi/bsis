package model.usage;

import constraintvalidator.ComponentExists;
import model.component.Component;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.user.User;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Date;

/**
 * We cannot use the name Usage for this class as Usage is
 * a keyword in MySQL.
 *
 * @author iamrohitbanga
 */
@Entity
@Audited
public class ComponentUsage implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, updatable = false, insertable = false)
  private Long id;

  @ComponentExists
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

  @Valid
  private RowModificationTracker modificationTracker;

  private Boolean isDeleted;

  @Column(length = 30)
  private String usedBy;

  public ComponentUsage() {
    modificationTracker = new RowModificationTracker();
  }

  @Override
  public Date getLastUpdated() {
    return modificationTracker.getLastUpdated();
  }

  @Override
  public void setLastUpdated(Date lastUpdated) {
    modificationTracker.setLastUpdated(lastUpdated);
  }

  @Override
  public Date getCreatedDate() {
    return modificationTracker.getCreatedDate();
  }

  @Override
  public void setCreatedDate(Date createdDate) {
    modificationTracker.setCreatedDate(createdDate);
  }

  @Override
  public User getCreatedBy() {
    return modificationTracker.getCreatedBy();
  }

  @Override
  public void setCreatedBy(User createdBy) {
    modificationTracker.setCreatedBy(createdBy);
  }

  @Override
  public User getLastUpdatedBy() {
    return modificationTracker.getLastUpdatedBy();
  }

  @Override
  public void setLastUpdatedBy(User lastUpdatedBy) {
    modificationTracker.setLastUpdatedBy(lastUpdatedBy);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getHospital() {
    return hospital;
  }

  public void setHospital(String hospital) {
    this.hospital = hospital;
  }

  public String getPatientName() {
    return patientName;
  }

  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public String getWard() {
    return ward;
  }

  public void setWard(String ward) {
    this.ward = ward;
  }

  public String getUseIndication() {
    return useIndication;
  }

  public void setUseIndication(String useIndication) {
    this.useIndication = useIndication;
  }

  public Date getUsageDate() {
    return usageDate;
  }

  public void setUsageDate(Date usageDate) {
    this.usageDate = usageDate;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Component getComponent() {
    return component;
  }

  public void setComponent(Component component) {
    this.component = component;
  }

  public RowModificationTracker getModificationTracker() {
    return modificationTracker;
  }

  public void setModificationTracker(RowModificationTracker modificationTracker) {
    this.modificationTracker = modificationTracker;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
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
package model.bloodtesting;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import model.collectedsample.CollectedSample;
import model.microtiterplate.MachineReading;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.user.User;

/**
 * Stores the result of one blood typing test for one collection.
 * No need to audit this table. NEVER update the entities of this table.
 * Always insert a new row. Use testedOn to find the latest test result.
 * @author iamrohitbanga
 */
@Entity
public class BloodTestResult implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false)
  private Long id;

  @ManyToOne
  private CollectedSample collectedSample;

  @ManyToOne
  private BloodTest bloodTest;

  @Column(length=10)
  private String result;

  @Temporal(TemporalType.TIMESTAMP)
  private Date testedOn;

  @OneToOne
  private MachineReading machineReading;

  @Valid
  private RowModificationTracker modificationTracker;

  @Lob
  private String notes;

  public BloodTestResult() {
    modificationTracker = new RowModificationTracker();
  }

  public Long getId() {
    return id;
  }

  public BloodTest getBloodTest() {
    return bloodTest;
  }

  public String getResult() {
    return result;
  }

  public String getNotes() {
    return notes;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setBloodTest(BloodTest bloodTest) {
    this.bloodTest = bloodTest;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public CollectedSample getCollectedSample() {
    return collectedSample;
  }

  public void setCollectedSample(CollectedSample collectedSample) {
    this.collectedSample = collectedSample;
  }

  public Date getTestedOn() {
    return testedOn;
  }

  public void setTestedOn(Date testedOn) {
    this.testedOn = testedOn;
  }

  public Date getLastUpdated() {
    return modificationTracker.getLastUpdated();
  }

  public Date getCreatedDate() {
    return modificationTracker.getCreatedDate();
  }

  public User getCreatedBy() {
    return modificationTracker.getCreatedBy();
  }

  public User getLastUpdatedBy() {
    return modificationTracker.getLastUpdatedBy();
  }

  public void setLastUpdated(Date lastUpdated) {
    modificationTracker.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    modificationTracker.setCreatedDate(createdDate);
  }

  public void setCreatedBy(User createdBy) {
    modificationTracker.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    modificationTracker.setLastUpdatedBy(lastUpdatedBy);
  }

  public MachineReading getMachineReading() {
    return machineReading;
  }

  public void setMachineReading(MachineReading machineReading) {
    this.machineReading = machineReading;
  }
}

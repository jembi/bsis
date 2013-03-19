package model.testresults;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.hibernate.envers.Audited;

import model.bloodtest.BloodTest;
import model.collectedsample.CollectedSample;
import model.collectedsample.CollectedSampleExists;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.rawtestresult.RawTestResult;
import model.user.User;

@Entity
@Audited
public class TestResult implements ModificationTracker {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false)
	private Long id;

	@CollectedSampleExists
	@ManyToOne(optional=false)
	private CollectedSample collectedSample;

  @Temporal(TemporalType.TIMESTAMP)
	private Date testedOn;

  @ManyToOne
	private BloodTest bloodTest;

  @Column(length=255)
  private String result;

  @OneToMany(mappedBy="inferredTestResult")
  private List<RawTestResult> inferredFromTestResults;

  @Valid
  private RowModificationTracker modificationTracker;

	@Lob
	private String notes;

  private Boolean isDeleted;

  public TestResult() {
    modificationTracker = new RowModificationTracker();
  }

  public Long getId() {
    return id;
  }

  public void copy(TestResult testResult) {
    assert (this.id == testResult.id);
    this.bloodTest = testResult.bloodTest;
    this.result = testResult.result;
    this.testedOn = testResult.testedOn;
    this.notes = testResult.notes;
    this.isDeleted = testResult.isDeleted;
  }

  public CollectedSample getCollectedSample() {
    return collectedSample;
  }

  public Date getTestedOn() {
    return testedOn;
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

  public Boolean getIsDeleted() {
    return isDeleted;
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

  public void setId(Long id) {
    this.id = id;
  }

  public void setCollectedSample(CollectedSample collectedSample) {
    this.collectedSample = collectedSample;
  }

  public void setTestedOn(Date testedOn) {
    this.testedOn = testedOn;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void setBloodTest(BloodTest bloodTest) {
    this.bloodTest = bloodTest;
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

  public String getCollectionNumber() {
    if (collectedSample == null)
      return null;
    return collectedSample.getCollectionNumber();
  }

  public List<RawTestResult> getInferredFromTestResults() {
    return inferredFromTestResults;
  }

  public void setInferredFromTestResults(List<RawTestResult> inferredFromTestResults) {
    this.inferredFromTestResults = inferredFromTestResults;
  }
}
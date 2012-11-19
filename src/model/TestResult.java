package model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.collectedsample.CollectedSample;
import model.user.User;

@Entity
public class TestResult {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false)
	private Long id;

	@ManyToOne(optional=false)
	private CollectedSample collectedSample;

  @Temporal(TemporalType.TIMESTAMP)
	private Date testedOn;

	private String name;
	private String result;

  @Temporal(TemporalType.TIMESTAMP)
  private Date lastUpdated;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @ManyToOne
  private User createdBy;

  @ManyToOne
  private User lastUpdatedBy;

	@Lob
	private String notes;

  private Boolean isDeleted;

  public TestResult() {
  }

  public Long getId() {
    return id;
  }

  public CollectedSample getCollectedSample() {
    return collectedSample;
  }

  public Date getTestedOn() {
    return testedOn;
  }

  public String getName() {
    return name;
  }

  public String getResult() {
    return result;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public User getCreatedBy() {
    return createdBy;
  }

  public User getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public String getNotes() {
    return notes;
  }

  public Boolean getIsDeleted() {
    return isDeleted;
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

  public void setName(String testName) {
    this.name = testName;
  }

  public void setResult(String testResult) {
    this.result = testResult;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public void copy(TestResult otherTestResult) {
  }
}
package model.rawtestresult;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.rawbloodtest.RawBloodTest;
import model.testresults.TestResult;
import model.user.User;

@Entity
@Audited
public class RawTestResult implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false, insertable=false, updatable=false, columnDefinition="TINYINT")
  private Integer id;

  @ManyToOne
  private RawBloodTest rawBloodTest;

  @ManyToOne
  private TestResult inferredTestResult;

  @Column(length=10)
  private String rawTestResult;

  @Embedded
  private RowModificationTracker modificationTracker;

  private Boolean isDeleted;

  public RawTestResult() {
    modificationTracker = new RowModificationTracker();
  }

  public Integer getId() {
    return id;
  }

  public RawBloodTest getRawBloodTest() {
    return rawBloodTest;
  }

  public String getRawTestResult() {
    return rawTestResult;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setRawBloodTest(RawBloodTest rawBloodTest) {
    this.rawBloodTest = rawBloodTest;
  }

  public void setRawTestResult(String rawTestResult) {
    this.rawTestResult = rawTestResult;
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

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public TestResult getInferredTestResult() {
    return inferredTestResult;
  }

  public void setInferredTestResult(TestResult inferredTestResult) {
    this.inferredTestResult = inferredTestResult;
  }
}

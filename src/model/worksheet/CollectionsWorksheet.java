package model.worksheet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import model.collectedsample.CollectedSample;
import model.modificationtracker.ModificationTracker;
import model.modificationtracker.RowModificationTracker;
import model.testresults.TestResult;
import model.user.User;

@Entity
public class CollectionsWorksheet implements ModificationTracker {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable=false, updatable=false, insertable=false)
  private Long id;

  private String worksheetBatchId;

  @ManyToMany(mappedBy="worksheets")
  private List<CollectedSample> collectedSamples;

  /** Associate worksheet to all the corresponding test results.
   *  Test results can be entered independently of the worksheets.
   *  However worksheets should be linked to specific test result
   *  rows otherwise in the worksheet edit form we will not know
   *  whether the test result already exists or not.
   */
  @OneToMany(mappedBy="worksheet")
  private List<TestResult> testResults;
  
  @Valid
  private RowModificationTracker modificationTracker;

  public CollectionsWorksheet() {
    modificationTracker = new RowModificationTracker();
    collectedSamples = new ArrayList<CollectedSample>();
    testResults = new ArrayList<TestResult>();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getWorksheetBatchId() {
    return worksheetBatchId;
  }

  public void setWorksheetBatchId(String worksheetBatchId) {
    this.worksheetBatchId = worksheetBatchId;
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

  public List<CollectedSample> getCollectedSamples() {
    return collectedSamples;
  }

  public void setCollectedSamples(List<CollectedSample> collectedSamples) {
    this.collectedSamples = collectedSamples;
  }

  public List<TestResult> getTestResults() {
    return testResults;
  }

  public void setTestResults(List<TestResult> testResults) {
    this.testResults = testResults;
  }
}

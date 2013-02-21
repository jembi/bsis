package viewmodel;

import java.util.Date;

import model.CustomDateFormatter;
import model.bloodtest.BloodTest;
import model.collectedsample.CollectedSample;
import model.testresults.TestResult;
import model.user.User;

public class TestResultViewModel {

	private TestResult testResult;

	public TestResultViewModel() {
	}

	public TestResultViewModel(TestResult testResult) {
		this.testResult = testResult;
	}

  public boolean equals(Object obj) {
    return testResult.equals(obj);
  }

  public Long getId() {
    return testResult.getId();
  }

  public CollectedSample getCollectedSample() {
    return testResult.getCollectedSample();
  }

  public String getTestedOn() {
    if (testResult.getTestedOn() == null)
      return ""; 
    return CustomDateFormatter.getDateTimeString(testResult.getTestedOn());
  }

  public BloodTest getBloodTest() {
    return testResult.getBloodTest();
  }

  public String getResult() {
    return testResult.getResult();
  }

  public String getNotes() {
    return testResult.getNotes();
  }

  public Boolean getIsDeleted() {
    return testResult.getIsDeleted();
  }

  public int hashCode() {
    return testResult.hashCode();
  }

  public void setId(Long id) {
    testResult.setId(id);
  }

  public void setCollectedSample(CollectedSample collectedSample) {
    testResult.setCollectedSample(collectedSample);
  }

  public void setTestedOn(Date testedOn) {
    testResult.setTestedOn(testedOn);
  }

  public void setResult(String bloodTestResult) {
    testResult.setResult(bloodTestResult);
  }

  public void setLastUpdated(Date lastUpdated) {
    testResult.setLastUpdated(lastUpdated);
  }

  public void setCreatedDate(Date createdDate) {
    testResult.setCreatedDate(createdDate);
  }

  public void setCreatedBy(User createdBy) {
    testResult.setCreatedBy(createdBy);
  }

  public void setLastUpdatedBy(User lastUpdatedBy) {
    testResult.setLastUpdatedBy(lastUpdatedBy);
  }

  public void setNotes(String notes) {
    testResult.setNotes(notes);
  }

  public void setIsDeleted(Boolean isDeleted) {
    testResult.setIsDeleted(isDeleted);
  }

  public void setBloodTest(BloodTest bloodTest) {
    testResult.setBloodTest(bloodTest);
  }

  public String toString() {
    return testResult.toString();
  }

  public String getLastUpdated() {
    return CustomDateFormatter.getDateTimeString(testResult.getLastUpdated());
  }

  public String getCreatedDate() {
    return CustomDateFormatter.getDateTimeString(testResult.getCreatedDate());
  }

  public String getCreatedBy() {
    User user = testResult.getCreatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }

  public String getLastUpdatedBy() {
    User user = testResult.getLastUpdatedBy();
    if (user == null || user.getUsername() == null)
      return "";
    return user.getUsername();
  }
}

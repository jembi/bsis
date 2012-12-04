package model.testresults;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.CustomDateFormatter;
import model.collectedsample.CollectedSample;
import model.user.User;

public class TestResultBackingForm {

  private TestResult testResult;

  private String dateTestedFrom;
  private String dateTestedTo;

  private String collectionNumber;
  private List<String> tests;

  private String testedOn;

  public TestResultBackingForm() {
    testResult = new TestResult();
  }

  public TestResultBackingForm(TestResult testResult) {
    this.testResult = testResult;
  }

  public void copy(TestResult otherTestResult) {
    testResult.copy(otherTestResult);
  }

  public boolean equals(Object obj) {
    return testResult.equals(obj);
  }

  public String getTestedOn() {
    if (testedOn != null)
      return testedOn;
    if (testResult == null)
      return "";
    return CustomDateFormatter.getDateString(testResult.getTestedOn());

  }

  public void setTestedOn(String testedOn) {
    this.testedOn = testedOn;
    try {
      testResult.setTestedOn(CustomDateFormatter.getDateFromString(testedOn));
    } catch (ParseException ex) {
      ex.printStackTrace();
      testResult.setTestedOn(null);
    }
  }

  public String getDateTestedFrom() {
    return dateTestedFrom;
  }

  public void setDateTestedFrom(String dateTestedFrom) {
    this.dateTestedFrom = dateTestedFrom;
  }

  public String getDateTestedTo() {
    return dateTestedTo;
  }

  public void setDateTestedTo(String dateTestedTo) {
    this.dateTestedTo = dateTestedTo;
  }

  public Long getId() {
    return testResult.getId();
  }

  public CollectedSample getCollectedSample() {
    return testResult.getCollectedSample();
  }

  public String getName() {
    return testResult.getName();
  }

  public String getResult() {
    return testResult.getResult();
  }

  public Date getLastUpdated() {
    return testResult.getLastUpdated();
  }

  public Date getCreatedDate() {
    return testResult.getCreatedDate();
  }

  public User getCreatedBy() {
    return testResult.getCreatedBy();
  }

  public User getLastUpdatedBy() {
    return testResult.getLastUpdatedBy();
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

  public void setName(String testName) {
    testResult.setName(testName);
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

  public String toString() {
    return testResult.toString();
  }

  public TestResult getTestResult() {
    return testResult;
  }

  public void setTestResult(TestResult testResult) {
    this.testResult = testResult;
  }

  public List<String> getTests() {
    return tests;
  }

  public String getCollectionNumber() {
    return collectionNumber;
  }

  public void setCollectionNumber(String collectionNumber) {
    this.collectionNumber = collectionNumber;
  }

}
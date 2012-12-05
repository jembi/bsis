package model.testresults;

import java.text.ParseException;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import model.CustomDateFormatter;
import model.bloodtest.BloodTest;
import model.bloodtest.BloodTestResult;
import model.collectedsample.CollectedSample;
import model.user.User;

public class TestResultBackingForm {

  @NotNull
  @Valid
  private TestResult testResult;

  private String dateTestedFrom;
  private String dateTestedTo;

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

  public String getBloodTest() {
    if (testResult == null ||
        testResult.getBloodTest() == null ||
        testResult.getBloodTest().getName() == null)
      return "";
    return testResult.getBloodTest().getName();
  }

  public String getBloodTestResult() {
    if (testResult == null ||
        testResult.getBloodTestResult() == null ||
        testResult.getBloodTestResult().getResult() == null)
      return "";
    return testResult.getBloodTestResult().getResult();
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

  public void setTestedOn(Date testedOn) {
    testResult.setTestedOn(testedOn);
  }

  public void setBloodTest(String bloodTestName) {
    if (testResult == null)
      return;
    BloodTest bloodTest = new BloodTest();
    bloodTest.setName(bloodTestName);
    testResult.setBloodTest(bloodTest);
  }

  public void setBloodTestResult(String result) {
    if (testResult == null)
      return;
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setId(Long.parseLong(result));
    testResult.setBloodTestResult(bloodTestResult);
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

  public String getCollectionNumber() {
    if (testResult == null || testResult.getCollectedSample() == null ||
        testResult.getCollectedSample().getCollectionNumber() == null
       )
      return "";
    return testResult.getCollectedSample().getCollectionNumber();
  }

  public void setCollectionNumber(String collectionNumber) {
    CollectedSample collectedSample = new CollectedSample();
    collectedSample.setCollectionNumber(collectionNumber);
    testResult.setCollectedSample(collectedSample);
  }

  public void setCollectedSample(CollectedSample collectedSample) {
    testResult.setCollectedSample(collectedSample);
  }
}
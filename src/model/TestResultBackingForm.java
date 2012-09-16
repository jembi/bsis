package model;

import java.util.Date;

public class TestResultBackingForm {
  private TestResult testResult;
  private String dateTestedFrom;
  private String dateTestedTo;

  public TestResultBackingForm() {
    testResult = new TestResult();
  }

  public void copy(TestResult otherTestResult) {
    testResult.copy(otherTestResult);
  }

  public boolean equals(Object obj) {
    return testResult.equals(obj);
  }

  public Long getTestResultId() {
    return testResult.getTestResultId();
  }

  public String getCollectionNumber() {
    return testResult.getCollectionNumber();
  }

  public Date getDateCollected() {
    return testResult.getDateCollected();
  }

  public Date getDateTested() {
    return testResult.getDateTested();
  }

  public String getComments() {
    return testResult.getComments();
  }

  public String getHiv() {
    return testResult.getHiv();
  }

  public String getHbv() {
    return testResult.getHbv();
  }

  public String getHcv() {
    return testResult.getHcv();
  }

  public String getSyphilis() {
    return testResult.getSyphilis();
  }

  public String getAbo() {
    return testResult.getAbo();
  }

  public String getRhd() {
    return testResult.getRhd();
  }

  public Boolean getIsDeleted() {
    return testResult.getIsDeleted();
  }

  public int hashCode() {
    return testResult.hashCode();
  }

  public void setCollectionNumber(String collectionNumber) {
    testResult.setCollectionNumber(collectionNumber);
  }

  public void setDateCollected(Date dateCollected) {
    testResult.setDateCollected(dateCollected);
  }

  public void setDateTested(Date dateTested) {
    testResult.setDateTested(dateTested);
  }

  public void setComments(String comments) {
    testResult.setComments(comments);
  }

  public void setHiv(String hiv) {
    testResult.setHiv(hiv);
  }

  public void setHbv(String hbv) {
    testResult.setHbv(hbv);
  }

  public void setHcv(String hcv) {
    testResult.setHcv(hcv);
  }

  public void setSyphilis(String syphilis) {
    testResult.setSyphilis(syphilis);
  }

  public void setAbo(String abo) {
    testResult.setAbo(abo);
  }

  public void setRhd(String rhd) {
    testResult.setRhd(rhd);
  }

  public void setIsDeleted(Boolean deleted) {
    testResult.setIsDeleted(deleted);
  }

  public String toString() {
    return testResult.toString();
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
}

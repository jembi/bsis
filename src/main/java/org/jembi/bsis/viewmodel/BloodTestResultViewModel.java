package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.Map;

import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.utils.CustomDateFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BloodTestResultViewModel {

  @JsonIgnore
  private BloodTestResult testResult;

  private Map<String, Boolean> permissions;

  public BloodTestResultViewModel() {
    testResult = new BloodTestResult();
  }

  public BloodTestResultViewModel(BloodTestResult testResult) {
    this.testResult = testResult;
  }

  public BloodTestResult getTestResult() {
    return testResult;
  }

  public void setTestResult(BloodTestResult testResult) {
    this.testResult = testResult;
  }

  public Long getId() {
    return testResult.getId();
  }
  
  public void setId(long id) {
    testResult.setId(id);
  }

  public BloodTestViewModel getBloodTest() {
    return new BloodTestViewModel(testResult.getBloodTest());
  }

  public Boolean getReEntryRequired () {
    return testResult.getReEntryRequired();
  }

  public String getNotes() {
    return testResult.getNotes();
  }

  public String getResult() {
    return testResult.getResult();
  }

  public String getTestedOn() {
    Date testedOn = testResult.getTestedOn();
    if (testedOn != null) {
      return CustomDateFormatter.getDateTimeString(testedOn);
    } else {
      return "";
    }
  }

  public String getReagentLotNumber() {
    return testResult.getReagentLotNumber();
  }

  public Map<String, Boolean> getPermissions() {
    return permissions;
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }
}
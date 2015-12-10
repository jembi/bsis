package viewmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import model.bloodtesting.BloodTestResult;
import utils.CustomDateFormatter;

import java.util.Date;
import java.util.Map;

public class BloodTestResultViewModel {

  @JsonIgnore
  private BloodTestResult testResult;

  private Map<String, Boolean> permissions;

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

  public BloodTestViewModel getBloodTest() {
    return new BloodTestViewModel(testResult.getBloodTest());
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
      return CustomDateFormatter.getDateString(testedOn);
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
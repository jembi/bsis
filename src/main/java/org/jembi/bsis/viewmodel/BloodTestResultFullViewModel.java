package org.jembi.bsis.viewmodel;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class BloodTestResultFullViewModel extends BaseViewModel<UUID> {

  private BloodTestFullViewModel bloodTest;
  private Boolean reEntryRequired;
  private String result;
  private Date testedOn;

  private Map<String, Boolean> permissions;

  public BloodTestResultFullViewModel() {
  }

  public BloodTestFullViewModel getBloodTest() {
    return bloodTest;
  }

  public Boolean getReEntryRequired() {
    return reEntryRequired;
  }

  public String getResult() {
    return result;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getTestedOn() {
    return testedOn;
  }

  public Map<String, Boolean> getPermissions() {
    return permissions;
  }

  public void setBloodTest(BloodTestFullViewModel bloodTest) {
    this.bloodTest = bloodTest;
  }

  public void setReEntryRequired(Boolean reEntryRequired) {
    this.reEntryRequired = reEntryRequired;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public void setTestedOn(Date testedOn) {
    this.testedOn = testedOn;
  }

  public void setPermissions(Map<String, Boolean> permissions) {
    this.permissions = permissions;
  }
}
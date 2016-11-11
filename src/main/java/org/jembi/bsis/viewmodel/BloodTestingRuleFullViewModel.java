package org.jembi.bsis.viewmodel;

import java.util.Set;

public class BloodTestingRuleFullViewModel extends BloodTestingRuleViewModel {

  private BloodTestFullViewModel bloodTest;

  private Set<String> pendingTestsIds;

  public BloodTestFullViewModel getBloodTest () {
    return bloodTest;
  }

  public void setBloodTest (BloodTestFullViewModel bloodTest) {
    this.bloodTest = bloodTest;
  }

  public Set<String> getPendingTestsIds () {
    return pendingTestsIds;
  }

  public void setPendingTestsIds (Set<String> pendingTestsIds) {
    this.pendingTestsIds = pendingTestsIds;
  }
}

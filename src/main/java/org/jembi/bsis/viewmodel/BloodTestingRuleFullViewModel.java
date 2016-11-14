package org.jembi.bsis.viewmodel;

import java.util.Set;

public class BloodTestingRuleFullViewModel extends BloodTestingRuleViewModel {

  private BloodTestFullViewModel bloodTest;

  private Set<Long> pendingTestsIds;

  public BloodTestFullViewModel getBloodTest () {
    return bloodTest;
  }

  public void setBloodTest (BloodTestFullViewModel bloodTest) {
    this.bloodTest = bloodTest;
  }

  public Set<Long> getPendingTestsIds () {
    return pendingTestsIds;
  }

  public void setPendingTestsIds (Set<Long> pendingTestsIds) {
    this.pendingTestsIds = pendingTestsIds;
  }
}

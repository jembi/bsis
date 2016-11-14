package org.jembi.bsis.viewmodel;

import java.util.Set;

public class BloodTestingRuleFullViewModel extends BloodTestingRuleViewModel {

  private BloodTestFullViewModel bloodTest;

  private Set<BloodTestViewModel> pendingTests;

  public BloodTestFullViewModel getBloodTest() {
    return bloodTest;
  }

  public void setBloodTest(BloodTestFullViewModel bloodTest) {
    this.bloodTest = bloodTest;
  }

  public Set<BloodTestViewModel> getPendingTests() {
    return pendingTests;
  }

  public void setPendingTests(Set<BloodTestViewModel> pendingTests) {
    this.pendingTests = pendingTests;
  }
}

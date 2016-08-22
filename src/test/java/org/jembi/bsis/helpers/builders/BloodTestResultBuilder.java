package org.jembi.bsis.helpers.builders;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.donation.Donation;

public class BloodTestResultBuilder extends AbstractEntityBuilder<BloodTestResult> {

  private Long id;
  private String result;
  private BloodTest bloodTest;
  private Donation donation;
  private boolean reEntryRequired;
  private boolean isDeleted = false;

  public BloodTestResultBuilder withId(Long id) {
    this.id = id;
    return this;
  }

  public BloodTestResultBuilder withResult(String result) {
    this.result = result;
    return this;
  }

  public BloodTestResultBuilder withBloodTest(BloodTest bloodTest) {
    this.bloodTest = bloodTest;
    return this;
  }
  
  public BloodTestResultBuilder withDonation(Donation donation) {
    this.donation = donation;
    return this;
  }

  public BloodTestResultBuilder withReEntryRequired(boolean reEntryRequired) {
    this.reEntryRequired = reEntryRequired;
    return this;
  }

  public BloodTestResultBuilder withIsDeleted(boolean isDeleted) {
    this.isDeleted = isDeleted;
    return this;
  }

  @Override
  public BloodTestResult build() {
    BloodTestResult bloodTestResult = new BloodTestResult();
    bloodTestResult.setId(id);
    bloodTestResult.setResult(result);
    bloodTestResult.setBloodTest(bloodTest);
    bloodTestResult.setDonation(donation);
    bloodTestResult.setReEntryRequired(reEntryRequired);
    bloodTestResult.setIsDeleted(isDeleted);
    return bloodTestResult;
  }

  public static BloodTestResultBuilder aBloodTestResult() {
    return new BloodTestResultBuilder();
  }

}

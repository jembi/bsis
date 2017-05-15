package org.jembi.bsis.helpers.builders;

import java.util.Map;
import java.util.UUID;

import org.jembi.bsis.backingform.TestResultsBackingForm;

public class TestResultsBackingFormBuilder extends AbstractBuilder<TestResultsBackingForm> {

  private String donationIdentificationNumber;
  private Map<UUID, String> testResults;
  private boolean saveUninterpretableResults;


  public TestResultsBackingFormBuilder withDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }

  public TestResultsBackingFormBuilder withTestResults(Map<UUID, String> testResults) {
    this.testResults = testResults;
    return this;
  }

  public TestResultsBackingFormBuilder withSaveUninterpretableResults(boolean saveUninterpretableResults) {
    this.saveUninterpretableResults = saveUninterpretableResults;
    return this;
  }

  @Override
  public TestResultsBackingForm build() {
    TestResultsBackingForm backingForm = new TestResultsBackingForm();
    backingForm.setDonationIdentificationNumber(donationIdentificationNumber);
    backingForm.setTestResults(testResults);
    backingForm.setSaveUninterpretableResults(saveUninterpretableResults);
    return backingForm;
  }

  public static TestResultsBackingFormBuilder aTestResultsBackingForm() {
    return new TestResultsBackingFormBuilder();
  }

}

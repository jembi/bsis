package helpers.builders;

import java.util.Map;

import backingform.TestResultsBackingForm;

public class TestResultsBackingFormBuilder {

  private String donationIdentificationNumber;
  private Map<Long, String> testResults;
  private boolean saveUninterpretableResults;


  public TestResultsBackingFormBuilder withDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }

  public TestResultsBackingFormBuilder withTestResults(Map<Long, String> testResults) {
    this.testResults = testResults;
    return this;
  }

  public TestResultsBackingFormBuilder withSaveUninterpretableResults(boolean saveUninterpretableResults) {
    this.saveUninterpretableResults = saveUninterpretableResults;
    return this;
  }

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

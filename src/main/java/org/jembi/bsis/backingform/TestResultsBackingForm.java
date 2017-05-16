package org.jembi.bsis.backingform;

import java.util.Map;
import java.util.UUID;

public class TestResultsBackingForm {

  private String donationIdentificationNumber;
  private Map<UUID, String> testResults;
  private boolean saveUninterpretableResults;

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public Map<UUID, String> getTestResults() {
    return testResults;
  }

  public void setTestResults(Map<UUID, String> testResults) {
    this.testResults = testResults;
  }

  public boolean getSaveUninterpretableResults() {
    return saveUninterpretableResults;
  }

  public void setSaveUninterpretableResults(boolean saveUninterpretableResults) {
    this.saveUninterpretableResults = saveUninterpretableResults;
  }

}

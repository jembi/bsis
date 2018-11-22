package org.jembi.bsis.viewmodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.TTIStatus;

public class DonationTestOutcomesReportViewModel {
  
  private String donationIdentificationNumber;
  private BloodTypingStatus bloodTypingStatus;
  private TTIStatus ttiStatus;
  private String previousDonationAboRhOutcome;
  private boolean released;

  private Map<String, String> bloodTestOutcomes;

  public DonationTestOutcomesReportViewModel() {
    super();
  }

  public void setBloodTestOutcomes(Collection<BloodTestResult> bloodTestOutcomes) {
    Map<String, String> bloodTestOutcomesMap = new HashMap<String, String>();
    if (bloodTestOutcomes != null) {
      for (BloodTestResult bloodTestOutcome : bloodTestOutcomes) {
        bloodTestOutcomesMap.put(bloodTestOutcome.getBloodTest().getTestNameShort(), bloodTestOutcome.getResult());
      }
    }
    this.bloodTestOutcomes = bloodTestOutcomesMap;
  }

  public String getDonationIdentificationNumber() {
    return donationIdentificationNumber;
  }

  public void setDonationIdentificationNumber(String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
  }

  public BloodTypingStatus getBloodTypingStatus() {
    return bloodTypingStatus;
  }

  public void setBloodTypingStatus(BloodTypingStatus bloodTypingStatus) {
    this.bloodTypingStatus = bloodTypingStatus;
  }

  public TTIStatus getTtiStatus() {
    return ttiStatus;
  }

  public void setTtiStatus(TTIStatus ttiStatus) {
    this.ttiStatus = ttiStatus;
  }

  public String getPreviousDonationAboRhOutcome() {
    return previousDonationAboRhOutcome;
  }

  public void setPreviousDonationAboRhOutcome(String previousDonationAboRhOutcome) {
    this.previousDonationAboRhOutcome = previousDonationAboRhOutcome;
  }

  public Map<String, String> getBloodTestOutcomes() {
    return bloodTestOutcomes;
  }

  public void setBloodTestOutcomes(Map<String, String> bloodTestOutcomes) {
    this.bloodTestOutcomes = bloodTestOutcomes;
  }
  
  public boolean isReleased() {
    return released;
  }

  public void setReleased(boolean released) {
    this.released = released;
  }
}
package viewmodel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import model.bloodtesting.BloodTestResult;
import model.bloodtesting.TTIStatus;
import repository.bloodtesting.BloodTypingStatus;

public class DonationTestOutcomesReportViewModel {
  
  private String donationIdentificationNumber;
  private BloodTypingStatus bloodTypingStatus;
  private TTIStatus ttiStatus;
  private String previousDonationAboRhOutcome;

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

  @Override
  public String toString() {
    return "DonationTestOutcomesReportViewModel [donationIdentificationNumber=" + donationIdentificationNumber
        + ", bloodTypingStatus=" + bloodTypingStatus + ", ttiStatus=" + ttiStatus + ", previousDonationAboRhOutcome="
        + previousDonationAboRhOutcome + ", bloodTestOutcomes=" + bloodTestOutcomes + "]";
  }



}
package org.jembi.bsis.helpers.builders;

import java.util.Map;

import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.viewmodel.DonationTestOutcomesReportViewModel;

public class DonationTestOutcomesReportViewModelBuilder extends AbstractBuilder<DonationTestOutcomesReportViewModel> {

  private String donationIdentificationNumber;
  private BloodTypingStatus bloodTypingStatus;
  private TTIStatus ttiStatus;
  private String previousDonationAboRhOutcome;

  private Map<String, String> bloodTestOutcomes;

  public DonationTestOutcomesReportViewModelBuilder withDonationIdentificationNumber(
      String donationIdentificationNumber) {
    this.donationIdentificationNumber = donationIdentificationNumber;
    return this;
  }

  public DonationTestOutcomesReportViewModelBuilder withTTIStatus(TTIStatus ttiStatus) {
    this.ttiStatus = ttiStatus;
    return this;
  }

  public DonationTestOutcomesReportViewModelBuilder withBloodTypingStatus(BloodTypingStatus bloodTypingStatus) {
    this.bloodTypingStatus = bloodTypingStatus;
    return this;
  }

  public DonationTestOutcomesReportViewModelBuilder withPreviousDonationAboRhOutcome(
      String previousDonationAboRhOutcome) {
    this.previousDonationAboRhOutcome = previousDonationAboRhOutcome;
    return this;
  }

  public DonationTestOutcomesReportViewModelBuilder withBloodTestOutcomes(Map<String, String> bloodTestOutcomes) {
    this.bloodTestOutcomes = bloodTestOutcomes;
    return this;
  }


  @Override
  public DonationTestOutcomesReportViewModel build() {
    DonationTestOutcomesReportViewModel donationTestOutcomesReportViewModel = new DonationTestOutcomesReportViewModel();
    donationTestOutcomesReportViewModel.setBloodTestOutcomes(bloodTestOutcomes);
    donationTestOutcomesReportViewModel.setBloodTypingStatus(bloodTypingStatus);
    donationTestOutcomesReportViewModel.setDonationIdentificationNumber(donationIdentificationNumber);
    donationTestOutcomesReportViewModel.setTtiStatus(ttiStatus);
    donationTestOutcomesReportViewModel.setPreviousDonationAboRhOutcome(previousDonationAboRhOutcome);
    return donationTestOutcomesReportViewModel;
  }

  public static DonationTestOutcomesReportViewModelBuilder aDonationTestOutcomesReportViewModel() {
    return new DonationTestOutcomesReportViewModelBuilder();
  }

}

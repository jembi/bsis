package factory;

import java.util.ArrayList;
import java.util.List;

import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import repository.bloodtesting.BloodTypingMatchStatus;
import viewmodel.DonationSummaryViewModel;

/**
 * A factory for creating DonationSummaryViewModel objects.
 */
public class DonationSummaryViewModelFactory {


  /**
   * Creates a list of DonationSummaryViewModel objects from a testBatch.
   *
   * @param testBatch the test batch
   * @param bloodTypingMatchStatus the blood typing match status
   * @return the list< donation summary view model>
   */
  public static List<DonationSummaryViewModel> createDonationSummaryViewModels(TestBatch testBatch,
      BloodTypingMatchStatus bloodTypingMatchStatus) {
    List<DonationSummaryViewModel> donationSummaryViewModels = new ArrayList<>();
    for (DonationBatch donationBatch : testBatch.getDonationBatches()) {
      donationSummaryViewModels =
          addToList(donationSummaryViewModels, donationBatch.getDonations(), bloodTypingMatchStatus, false, false);
    }
    return donationSummaryViewModels;
  }


  /**
   * Creates a list of DonationSummaryViewModel objects from a list of donations.
   *
   * @param donations the donations
   * @return the list< donation summary view model>
   */
  public static List<DonationSummaryViewModel> createDonationSummaryViewModels(List<Donation> donations) {
    List<DonationSummaryViewModel> donationSummaryViewModels = new ArrayList<>();
    donationSummaryViewModels = addToList(donationSummaryViewModels, donations, null, true, true);
    return donationSummaryViewModels;
  }

  /**
   * Adds the to list.
   *
   * @param donationSummaryViewModels the donation summary view models
   * @param donations the donations
   * @param bloodTypingMatchStatus the blood typing match status
   * @param includeVenueInfo the include venue info
   * @param includeDonorInfo the include donor info
   * @return the list
   */
  private static List<DonationSummaryViewModel> addToList(List<DonationSummaryViewModel> donationSummaryViewModels,
      List<Donation> donations, BloodTypingMatchStatus bloodTypingMatchStatus, boolean includeVenueInfo,
      boolean includeDonorInfo) {
    for (Donation donation : donations) {
      if (bloodTypingMatchStatus == null || donation.getBloodTypingMatchStatus().equals(bloodTypingMatchStatus)) {
        donationSummaryViewModels.add(new DonationSummaryViewModel(donation, includeVenueInfo, includeDonorInfo));
      }
    }
    return donationSummaryViewModels;
  }

}

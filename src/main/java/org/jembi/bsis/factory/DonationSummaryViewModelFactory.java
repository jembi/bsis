package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.List;

import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.viewmodel.DonationSummaryViewModel;
import org.springframework.stereotype.Service;

/**
 * A factory for creating DonationSummaryViewModel objects.
 */
@Service
public class DonationSummaryViewModelFactory {


  /**
   * Creates a list of DonationSummaryViewModel objects from a test batch, with the option of
   * filtering by blood typing match status if the bloodTypingMatchStatus parameter is not null.
   *
   * @param testBatch the test batch
   * @param bloodTypingMatchStatus the blood typing match status
   * @return the list< donation summary view model>
   */
  public List<DonationSummaryViewModel> createDonationSummaryViewModels(
      TestBatch testBatch, BloodTypingMatchStatus bloodTypingMatchStatus) {
    List<DonationSummaryViewModel> donationSummaryViewModels = new ArrayList<>();
    for (DonationBatch donationBatch : testBatch.getDonationBatches()) {
      for (Donation donation : donationBatch.getDonations()) {
        if (bloodTypingMatchStatus == null || donation.getBloodTypingMatchStatus().equals(bloodTypingMatchStatus)) {
          donationSummaryViewModels.add(new DonationSummaryViewModel(donation));
        }
      }
    }
    return donationSummaryViewModels;
  }


  /**
   * Creates a list of DonationSummaryViewModel objects from a list of donations. The models are a
   * full summary, which means they include venue and donor info.
   *
   * @param donations the donations
   * @return the list< donation summary view model>
   */
  public List<DonationSummaryViewModel> createFullDonationSummaryViewModels(List<Donation> donations) {
    List<DonationSummaryViewModel> donationSummaryViewModels = new ArrayList<>();
    for (Donation donation : donations) {
      donationSummaryViewModels.add(new DonationSummaryViewModel(donation, true, true));
    }
    return donationSummaryViewModels;
  }

}

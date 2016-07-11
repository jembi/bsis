package org.jembi.bsis.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.service.TestBatchConstraintChecker;
import org.jembi.bsis.service.TestBatchConstraintChecker.CanReleaseResult;
import org.jembi.bsis.viewmodel.DonationBatchViewModel;
import org.jembi.bsis.viewmodel.DonationTestOutcomesReportViewModel;
import org.jembi.bsis.viewmodel.TestBatchFullViewModel;
import org.jembi.bsis.viewmodel.TestBatchViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A factory for creating TestBatchViewModel and TestBatchFullViewModel objects.
 */
@Service
public class TestBatchViewModelFactory {

  /** The donation batch view model factory. */
  @Autowired
  private DonationBatchViewModelFactory donationBatchViewModelFactory;

  /** The test batch constraint checker. */
  @Autowired
  private TestBatchConstraintChecker testBatchConstraintChecker;

  @Autowired
  private LocationViewModelFactory locationViewModelFactory;

  /**
   * Creates a list of basic view models for the given list of test batches.
   *
   * @param testBatches the test batches
   * @param isTestingSupervisor the is testing supervisor
   * @return the list< test batch view model>
   */
  public List<TestBatchViewModel> createTestBatchBasicViewModels(List<TestBatch> testBatches) {
    List<TestBatchViewModel> viewModels = new ArrayList<>();
    for (TestBatch testBatch : testBatches) {
      TestBatchViewModel testBatchViewModel = new TestBatchViewModel();
      populateBasicViewModel(testBatch, testBatchViewModel);
      viewModels.add(testBatchViewModel);
    }
    return viewModels;
  }

  /**
   * Creates a list of full view models for the given list of test batches.
   *
   * @param testBatch the test batch
   * @param isTestingSupervisor the is testing supervisor
   * @return the test batch full view model
   */
  public List<TestBatchFullViewModel> createTestBatchFullViewModels(List<TestBatch> testBatches,
      boolean isTestingSupervisor) {
    List<TestBatchFullViewModel> viewModels = new ArrayList<>();
    for (TestBatch testBatch : testBatches) {
      viewModels.add(createTestBatchFullViewModel(testBatch, isTestingSupervisor));
    }
    return viewModels;
  }

  /**
   * Creates a full view model for the given test batch.
   *
   * @param testBatch the test batch
   * @param isTestingSupervisor the is testing supervisor
   * @return the test batch full view model
   */
  public TestBatchFullViewModel createTestBatchFullViewModel(TestBatch testBatch, boolean isTestingSupervisor) {
    TestBatchFullViewModel testBatchViewModel = new TestBatchFullViewModel();
    populateFullViewModel(testBatch, testBatchViewModel, isTestingSupervisor);
    return testBatchViewModel;
  }

  /**
   * Populate basic view model.
   *
   * @param testBatch the test batch
   * @param testBatchViewModel the test batch view model
   */
  private void populateBasicViewModel(TestBatch testBatch, TestBatchViewModel testBatchViewModel) {
    testBatchViewModel.setId(testBatch.getId());
    testBatchViewModel.setStatus(testBatch.getStatus());
    testBatchViewModel.setBatchNumber(testBatch.getBatchNumber());
    testBatchViewModel.setCreatedDate(testBatch.getCreatedDate());
    testBatchViewModel.setLastUpdated(testBatch.getLastUpdated());
    testBatchViewModel.setNotes(testBatch.getNotes());
    testBatchViewModel.setLocation(locationViewModelFactory.createLocationViewModel(testBatch.getLocation()));

    // Calculate number of samples (only consider donations with test samples)
    int numSamples = 0;
    for (DonationBatch donationBatch : testBatch.getDonationBatches()) {
      for (Donation donation : donationBatch.getDonations()) {
        if (donation.getPackType().getTestSampleProduced()) {
          numSamples++;
        }
      }
    }
    testBatchViewModel.setNumSamples(numSamples);
  }

  /**
   * Populate full view model.
   *
   * @param testBatch the test batch
   * @param testBatchViewModel the test batch view model
   * @param isTestingSupervisor the is testing supervisor
   * @return the test batch full view model
   */
  private TestBatchFullViewModel populateFullViewModel(TestBatch testBatch, TestBatchFullViewModel testBatchViewModel,
      boolean isTestingSupervisor) {

    // First populate basic fields
    populateBasicViewModel(testBatch, testBatchViewModel);

    // Get list of donation view models with test samples
    List<DonationBatchViewModel> donationsWithTestSamples = new ArrayList<>();
    if (testBatch.getDonationBatches() != null) {
      for (DonationBatch donationBatch : testBatch.getDonationBatches()) {
        donationsWithTestSamples.add(
            donationBatchViewModelFactory.createDonationBatchViewModelWithTestSamples(donationBatch));
      }
    }
    testBatchViewModel.setDonationBatches(donationsWithTestSamples);

    // Check if this test batch can be released
    CanReleaseResult canReleaseResult = testBatchConstraintChecker.canReleaseTestBatch(testBatch);
    if (canReleaseResult.canRelease()) {
      // Include the number of donations ready for release
      testBatchViewModel.setReadyForReleaseCount(canReleaseResult.getReadyCount());
    }

    // Set permissions
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canRelease", isTestingSupervisor && canReleaseResult.canRelease());
    permissions.put("canClose", isTestingSupervisor && testBatchConstraintChecker.canCloseTestBatch(testBatch));
    permissions.put("canDelete", isTestingSupervisor && testBatchConstraintChecker.canDeleteTestBatch(testBatch));
    permissions.put("canEdit", isTestingSupervisor && testBatchConstraintChecker.canEditTestBatch(testBatch));
    permissions.put("canEditDonationBatches",
        isTestingSupervisor && testBatchConstraintChecker.canAddOrRemoveDonationBatch(testBatch));
    permissions.put("canReopen", isTestingSupervisor && testBatchConstraintChecker.canReopenTestBatch(testBatch));
    testBatchViewModel.setPermissions(permissions);

    return testBatchViewModel;
  }

  public List<DonationTestOutcomesReportViewModel> createDonationTestOutcomesReportViewModels(TestBatch testBatch) {

    List<DonationTestOutcomesReportViewModel> donationTestOutcomesReportViewModels = new ArrayList<>();

    for (DonationBatch donationBatch : testBatch.getDonationBatches()) {
      for (Donation donation : donationBatch.getDonations()) {
        DonationTestOutcomesReportViewModel donationTestOutcomesReportViewModel =
            new DonationTestOutcomesReportViewModel();
        donationTestOutcomesReportViewModel.setBloodTypingStatus(donation.getBloodTypingStatus());
        donationTestOutcomesReportViewModel.setTtiStatus(donation.getTTIStatus());
        donationTestOutcomesReportViewModel.setDonationIdentificationNumber(donation.getDonationIdentificationNumber());
        donationTestOutcomesReportViewModel.setBloodTestOutcomes(donation.getBloodTestResults());
        donationTestOutcomesReportViewModel.setPreviousDonationAboRhOutcome(getPreviousDonationAboRhOutcome(donation));
        donationTestOutcomesReportViewModels.add(donationTestOutcomesReportViewModel);
      }
    }
    return donationTestOutcomesReportViewModels;

  }

  private String getPreviousDonationAboRhOutcome(Donation thisDonation) {
  
    List<Donation> donorDonations = new ArrayList<Donation>(thisDonation.getDonor().getDonations());
    String aboRh = "";

    if (donorDonations.size() > 1) {
      // Order donations for that donor by date desc to be able to find the previous donation
      Collections.sort(donorDonations, new Comparator<Donation>() {
        public int compare(Donation d1, Donation d2) {
          return d2.getDonationDate().compareTo(d1.getDonationDate());
        }
      });
      for (Donation donation : donorDonations) {
        // Find previous donation and return abo/rh outcome
        if (donation.getDonationDate().before(thisDonation.getDonationDate())) {
          aboRh = donation.getBloodAbo() + donation.getBloodRh();
          break;
        }
      }
    }
    return aboRh;
  }
}

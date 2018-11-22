package org.jembi.bsis.factory;

import org.jembi.bsis.backingform.TestBatchBackingForm;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.service.TestBatchConstraintChecker;
import org.jembi.bsis.service.TestBatchConstraintChecker.CanReleaseResult;
import org.jembi.bsis.viewmodel.DonationFullViewModel;
import org.jembi.bsis.viewmodel.DonationTestOutcomesReportViewModel;
import org.jembi.bsis.viewmodel.TestBatchFullDonationViewModel;
import org.jembi.bsis.viewmodel.TestBatchFullViewModel;
import org.jembi.bsis.viewmodel.TestBatchViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A factory for creating TestBatchViewModel and TestBatchFullViewModel objects.
 */
@Service
public class TestBatchFactory {

  @Autowired
  private TestBatchConstraintChecker testBatchConstraintChecker;

  @Autowired
  private LocationFactory locationFactory;

  @Autowired
  private LocationRepository locationRepository;
  
  @Autowired
  private DonationFactory donationFactory;

  public TestBatch createEntity(TestBatchBackingForm backingForm) {
    TestBatch testBatch = new TestBatch();
    testBatch.setId(backingForm.getId());
    testBatch.setStatus(backingForm.getStatus());
    testBatch.setTestBatchDate(backingForm.getTestBatchDate());
    testBatch.setLocation(locationRepository.getLocation(backingForm.getLocation().getId()));
    testBatch.setBackEntry(backingForm.isBackEntry());
    return testBatch;
  }

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
   * @return the test batch full view model
   */
  public List<TestBatchFullViewModel> createTestBatchFullViewModels(List<TestBatch> testBatches) {
    List<TestBatchFullViewModel> viewModels = new ArrayList<>();
    for (TestBatch testBatch : testBatches) {
      viewModels.add(createTestBatchFullViewModel(testBatch));
    }
    return viewModels;
  }

  /**
   * Creates a full view model for the given test batch.
   *
   * @param testBatch the test batch
   * @return the test batch full view model
   */
  public TestBatchFullViewModel createTestBatchFullViewModel(TestBatch testBatch) {
    TestBatchFullViewModel testBatchViewModel = new TestBatchFullViewModel();
    populateFullViewModel(testBatch, testBatchViewModel);
    return testBatchViewModel;
  }

  /**
   * Creates a view model for the given test batch
   * which contains a full view model list of donations.
   *
   * @param testBatch the test batch
   * @param bloodTypingMatchStatus the blood typing match status
   * @return the test batch view model with a list of donation full view model
   */
  public TestBatchFullDonationViewModel createTestBatchFullDonationViewModel(TestBatch testBatch, BloodTypingMatchStatus bloodTypingMatchStatus) {
    TestBatchFullDonationViewModel testBatchFullDonationViewModel = new TestBatchFullDonationViewModel();
    testBatchFullDonationViewModel.setDonations(createDonationFullViewModels(testBatch, bloodTypingMatchStatus));
    testBatchFullDonationViewModel.setId(testBatch.getId());
    testBatchFullDonationViewModel.setTestBatchDate(testBatch.getTestBatchDate());
    return testBatchFullDonationViewModel;
  }

  public TestBatchFullViewModel createTestBatchFullViewModel(TestBatch testBatch, Set<String> dinsWithoutTestSamples,
                                                             Set<String> dinsInOtherTestBatches, Set<String> dinsInOpenDonationBatch) {
    TestBatchFullViewModel testBatchFullViewModel = createTestBatchFullViewModel(testBatch);
    testBatchFullViewModel.addAllDonationIdsWithoutTestSamples(dinsWithoutTestSamples);
    testBatchFullViewModel.addAllDonationIdsInOtherTestBatches(dinsInOtherTestBatches);
    testBatchFullViewModel.addAllDonationIdsInOpenDonationBatch(dinsInOpenDonationBatch);
    return testBatchFullViewModel;
  }

  /**
   * Creates a list of DonationFullViewModel objects from a test batch, with the option of filtering
   * by blood typing match status if the bloodTypingMatchStatus parameter is not null.
   *
   * @param testBatch the test batch
   * @param bloodTypingMatchStatus the blood typing match status
   * @return the list< donation summary view model>
   */
  public List<DonationFullViewModel> createDonationFullViewModels(TestBatch testBatch,
      BloodTypingMatchStatus bloodTypingMatchStatus) {
    List<DonationFullViewModel> donationFullViewModels = new ArrayList<>();
    for (Donation donation : testBatch.getDonations()) {
      if (bloodTypingMatchStatus == null || donation.getBloodTypingMatchStatus().equals(bloodTypingMatchStatus)) {
        donationFullViewModels.add(donationFactory.createDonationFullViewModelWithoutPermissions(donation));
      }
    }
    return donationFullViewModels;
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
    testBatchViewModel.setTestBatchDate(testBatch.getTestBatchDate());
    testBatchViewModel.setLastUpdated(testBatch.getLastUpdated());
    testBatchViewModel.setNotes(testBatch.getNotes());
    testBatchViewModel.setLocation(locationFactory.createViewModel(testBatch.getLocation()));
    testBatchViewModel.setBackEntry(testBatch.isBackEntry());

    // Calculate number of samples (only consider donations with test samples)
    int numSamples = 0;
    for (Donation donation : testBatch.getDonations()) {
      if (donation.getPackType().getTestSampleProduced()) {
        numSamples++;
      }
    }
    testBatchViewModel.setNumSamples(numSamples);
  }

  /**
   * Populate full view model.
   *
   * @param testBatch the test batch
   * @param testBatchViewModel the test batch view model
   * @return the test batch full view model
   */
  private TestBatchFullViewModel populateFullViewModel(TestBatch testBatch, TestBatchFullViewModel testBatchViewModel) {

    // First populate basic fields
    populateBasicViewModel(testBatch, testBatchViewModel);

    // Get list of donation view models
    testBatchViewModel.addAllDonations(donationFactory.createDonationViewModels(testBatch.getDonations()));

    // Check if this test batch can be released
    CanReleaseResult canReleaseResult = testBatchConstraintChecker.canReleaseTestBatch(testBatch);
    if (canReleaseResult.canRelease()) {
      // Include the number of donations ready for release
      testBatchViewModel.setReadyForReleaseCount(canReleaseResult.getReadyCount());
    }

    // Set permissions
    Map<String, Boolean> permissions = new HashMap<>();
    permissions.put("canRelease", canReleaseResult.canRelease());
    permissions.put("canClose", testBatchConstraintChecker.canCloseTestBatch(testBatch));
    permissions.put("canDelete", testBatchConstraintChecker.canDeleteTestBatch(testBatch));
    permissions.put("canEdit", testBatchConstraintChecker.canEditTestBatch(testBatch));
    permissions.put("canEditDonations", testBatch.isOpen());
    permissions.put("canReopen", testBatchConstraintChecker.canReopenTestBatch(testBatch));
    testBatchViewModel.putAllPermissions(permissions);

    return testBatchViewModel;
  }

  public List<DonationTestOutcomesReportViewModel> createDonationTestOutcomesReportViewModels(TestBatch testBatch) {

    List<DonationTestOutcomesReportViewModel> donationTestOutcomesReportViewModels = new ArrayList<>();
    for (Donation donation : testBatch.getDonations()) {
      DonationTestOutcomesReportViewModel donationTestOutcomesReportViewModel =
          new DonationTestOutcomesReportViewModel();
      donationTestOutcomesReportViewModel.setBloodTypingStatus(donation.getBloodTypingStatus());
      donationTestOutcomesReportViewModel.setTtiStatus(donation.getTTIStatus());
      donationTestOutcomesReportViewModel.setDonationIdentificationNumber(donation.getDonationIdentificationNumber());
      donationTestOutcomesReportViewModel.setBloodTestOutcomes(donation.getBloodTestResults());
      donationTestOutcomesReportViewModel.setPreviousDonationAboRhOutcome(getPreviousDonationAboRhOutcome(donation));
      donationTestOutcomesReportViewModel.setReleased(donation.isReleased());
      donationTestOutcomesReportViewModels.add(donationTestOutcomesReportViewModel);
    }
    return donationTestOutcomesReportViewModels;

  }

  private String getPreviousDonationAboRhOutcome(Donation thisDonation) {
    List<Donation> donorDonations = new ArrayList<>(thisDonation.getDonor().getDonations());
    String aboRh = "";

    if (donorDonations.size() > 1) {
      // Order donations for that donor by date desc to be able to find the previous donation
      donorDonations.sort((d1, d2) -> d2.getDonationDate().compareTo(d1.getDonationDate()));
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

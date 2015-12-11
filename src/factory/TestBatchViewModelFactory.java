package factory;

import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.TestBatchConstraintChecker;
import service.TestBatchConstraintChecker.CanReleaseResult;
import viewmodel.DonationBatchViewModel;
import viewmodel.TestBatchViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestBatchViewModelFactory {

  @Autowired
  private DonationBatchViewModelFactory donationBatchViewModelFactory;
  @Autowired
  private TestBatchConstraintChecker testBatchConstraintChecker;

  public List<TestBatchViewModel> createTestBatchViewModels(List<TestBatch> testBatches, boolean isTestingSupervisor) {
    List<TestBatchViewModel> viewModels = new ArrayList<>();

    for (TestBatch testBatch : testBatches) {
      viewModels.add(createTestBatchViewModelWithoutPermissions(testBatch, isTestingSupervisor));
    }

    return viewModels;
  }

  public TestBatchViewModel createTestBatchViewModel(TestBatch testBatch, boolean isTestingSupervisor) {
    TestBatchViewModel testBatchViewModel = createTestBatchViewModelWithoutPermissions(testBatch, isTestingSupervisor);

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
    permissions.put("canEditDonationBatches", isTestingSupervisor && testBatchConstraintChecker.canAddOrRemoveDonationBatch(testBatch));
    permissions.put("canReopen", isTestingSupervisor && testBatchConstraintChecker.canReopenTestBatch(testBatch));
    testBatchViewModel.setPermissions(permissions);

    return testBatchViewModel;
  }

  private TestBatchViewModel createTestBatchViewModelWithoutPermissions(TestBatch testBatch, boolean isTestingSupervisor) {
    TestBatchViewModel testBatchViewModel = new TestBatchViewModel();
    testBatchViewModel.setId(testBatch.getId());
    testBatchViewModel.setStatus(testBatch.getStatus());
    testBatchViewModel.setBatchNumber(testBatch.getBatchNumber());
    testBatchViewModel.setCreatedDate(testBatch.getCreatedDate());
    testBatchViewModel.setLastUpdated(testBatch.getLastUpdated());
    testBatchViewModel.setNotes(testBatch.getNotes());

    // Add all donation batch view models
    List<DonationBatchViewModel> donationBatchViewModels = new ArrayList<>();
    if (testBatch.getDonationBatches() != null) {
      for (DonationBatch donationBatch : testBatch.getDonationBatches()) {
        donationBatchViewModels.add(donationBatchViewModelFactory.createDonationBatchViewModel(donationBatch,
                true));
      }
    }
    testBatchViewModel.setDonationBatches(donationBatchViewModels);

    return testBatchViewModel;
  }
}

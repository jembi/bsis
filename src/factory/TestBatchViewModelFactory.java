package factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.TestBatchConstraintChecker;
import viewmodel.DonationBatchViewModel;
import viewmodel.TestBatchViewModel;

@Service
public class TestBatchViewModelFactory {
    
    @Autowired
    private DonationBatchViewModelFactory donationBatchViewModelFactory;
    @Autowired
    private TestBatchConstraintChecker testBatchConstraintChecker;
    
    public TestBatchViewModel createTestBatchViewModel(TestBatch testBatch) {
        return createTestBatchViewModel(testBatch, false);
    }
    
    public TestBatchViewModel createTestBatchViewModel(TestBatch testBatch, boolean isTestingSupervisor) {
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
        
        // Set permissions
        Map<String, Boolean> permissions = new HashMap<>();
        permissions.put("canRelease", isTestingSupervisor && testBatchConstraintChecker.canReleaseTestBatch(testBatch));
        testBatchViewModel.setPermissions(permissions);

        return testBatchViewModel;
    }

}

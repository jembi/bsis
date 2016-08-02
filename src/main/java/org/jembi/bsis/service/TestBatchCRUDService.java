package org.jembi.bsis.service;

import org.apache.log4j.Logger;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationBatchRepository;
import org.jembi.bsis.repository.TestBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TestBatchCRUDService {

  private static final Logger LOGGER = Logger.getLogger(TestBatchCRUDService.class);

  @Autowired
  private TestBatchRepository testBatchRepository;
  @Autowired
  private TestBatchConstraintChecker testBatchConstraintChecker;
  @Autowired
  private TestBatchStatusChangeService testBatchStatusChangeService;

  @Autowired
  private DonationBatchRepository donationBatchRepository;

  public TestBatch updateTestBatch(TestBatch updatedTestBatch) {

    TestBatch existingTestBatch = testBatchRepository.findTestBatchById(updatedTestBatch.getId());

    if (existingTestBatch.getStatus() != TestBatchStatus.CLOSED && !testBatchConstraintChecker.canEditTestBatch(existingTestBatch)) {
      throw new IllegalStateException("Test batch cannot be updated");
    }

    if (updatedTestBatch.getCreatedDate() != null) {
      existingTestBatch.setCreatedDate(updatedTestBatch.getCreatedDate());
    }

    if (updatedTestBatch.getDonationBatches() != null) {
      // unlink old donation batches
      for (DonationBatch donationBatch : existingTestBatch.getDonationBatches()) {
        if (!updatedTestBatch.getDonationBatches().contains(donationBatch)) {
          donationBatch.setTestBatch(null);
          donationBatchRepository.updateDonationBatch(donationBatch);
        }
      }
      // link new donation batches
      for (DonationBatch donationBatch : updatedTestBatch.getDonationBatches()) {
        donationBatch.setTestBatch(existingTestBatch);
        donationBatchRepository.updateDonationBatch(donationBatch);
      }
      existingTestBatch.setDonationBatches(updatedTestBatch.getDonationBatches());
    }
    
    existingTestBatch.setLocation(updatedTestBatch.getLocation());

    if (updatedTestBatch.getStatus() != null) {
      existingTestBatch = changeTestBatchStatus(existingTestBatch, updatedTestBatch.getStatus());
    }

    return testBatchRepository.update(existingTestBatch);
  }

  public void deleteTestBatch(Long testBatchId) {
    TestBatch testBatch = testBatchRepository.findTestBatchById(testBatchId);
    if (!testBatchConstraintChecker.canDeleteTestBatch(testBatch)) {
      throw new IllegalStateException("Test batch cannot be deleted");
    }
    testBatchRepository.deleteTestBatch(testBatchId);
  }

  protected TestBatch changeTestBatchStatus(TestBatch testBatch, TestBatchStatus newStatus) {
    LOGGER.info("Updating status of test batch " + testBatch.getId() + " to " + newStatus);

    TestBatchStatus oldStatus = testBatch.getStatus();
    if (newStatus == testBatch.getStatus()) {
      // The status is not being changed so return early
      return testBatch;
    }

    if (oldStatus == TestBatchStatus.OPEN && newStatus == TestBatchStatus.RELEASED
        && !testBatchConstraintChecker.canReleaseTestBatch(testBatch).canRelease()) {
      throw new IllegalStateException("Test batch cannot be released");
    }

    if (newStatus == TestBatchStatus.CLOSED && !testBatchConstraintChecker.canCloseTestBatch(testBatch)) {
      throw new IllegalStateException("Only released test batches can be closed");
    }

    if (newStatus == TestBatchStatus.OPEN && !testBatchConstraintChecker.canReopenTestBatch(testBatch)) {
      throw new IllegalStateException("Only closed test batches can be reopened");
    }

    // Set the new status
    testBatch.setStatus(newStatus);

    testBatch = testBatchRepository.update(testBatch);

    if (oldStatus == TestBatchStatus.OPEN && newStatus == TestBatchStatus.RELEASED) {
      testBatchStatusChangeService.handleRelease(testBatch);
    }

    return testBatch;
  }
}

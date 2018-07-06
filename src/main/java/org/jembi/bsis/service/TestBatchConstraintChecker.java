package org.jembi.bsis.service;

import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestBatchConstraintChecker {

  @Autowired
  private DonationConstraintChecker donationConstraintChecker;
  @Autowired
  private BloodTestsService bloodTestsService;

  /**
   * A test batch can be released if it is open and none of the donations have outstanding
   * outcomes.
   */
  public CanReleaseResult canReleaseTestBatch(TestBatch testBatch) {

    // Only open test batches with assigned donations can be released
    if (testBatch.getStatus() != TestBatchStatus.OPEN || testBatch.getDonations() == null
        || testBatch.getDonations().isEmpty()) {
      return new CanReleaseResult(false);
    }

    int readyCount = 0;

    // Check for tests with outstanding test outcomes
    for (Donation donation : testBatch.getDonations()) {

      if (!donation.getPackType().getTestSampleProduced()) {
        // Don't consider donations without test samples
        continue;
      }

      BloodTestingRuleResult bloodTestingRuleResult = bloodTestsService.executeTests(donation);

      if (donationConstraintChecker.donationHasOutstandingOutcomes(donation, bloodTestingRuleResult)) {
        // This test has an outstanding outcome
        return new CanReleaseResult(false);
      }

      if (!donationConstraintChecker.donationHasDiscrepancies(donation, bloodTestingRuleResult)) {
        // This donations is ready to be released
        readyCount++;
      }
    }

    return new CanReleaseResult(true, readyCount);
  }

  /**
   * Determines if a TestBatch can be marked as voided which is any time before a test result is
   * recorded for any of the donations in the donation batches.
   *
   * @param testBatch TestBatch to check
   * @return true if the specified TestBatch can be deleted
   */
  public boolean canDeleteTestBatch(TestBatch testBatch) {
    return testBatchHasResults(testBatch) == false;
  }

  /**
   * A Test Batch can be reopened if it has been closed.
   */
  public boolean canReopenTestBatch(TestBatch testBatch) {
    if (testBatch.getStatus() == TestBatchStatus.CLOSED) {
      return true;
    }
    return false;
  }

  /**
   * A Test Batch can be edited if it is open
   */
  public boolean canEditTestBatch(TestBatch testBatch) {
    if (testBatch.getStatus() != TestBatchStatus.CLOSED) {
      return true;
    }
    return false;
  }

  /**
   * A test batch can be closed if it is released and none of the donations have discrepancies.
   */
  public boolean canCloseTestBatch(TestBatch testBatch) {

    if (testBatch.getStatus() != TestBatchStatus.RELEASED) {
      // Only released batches can be closed
      return false;
    }

    if (testBatch.getDonations() != null) {

        for (Donation donation : testBatch.getDonations()) {

          if (donationConstraintChecker.donationHasDiscrepancies(donation)) {

            // This donation has discrepancies
            return false;
          }
        }
      }
    return true;
  }

  protected boolean testBatchHasResults(TestBatch testBatch) {
    if (testBatch.getDonations() != null) {
        for (Donation donation : testBatch.getDonations()) {
          if (donationConstraintChecker.donationHasSavedTestResults(donation)) {
            // test results have been recorded for this donation
            return true;
          }
        }
      }
    return false;
  }

  public static class CanReleaseResult {

    private boolean canRelease;
    private int readyCount;

    /**
     * @param canRelease true if the test batch can be released, otherwise false.
     */
    public CanReleaseResult(boolean canRelease) {
      this.canRelease = canRelease;
    }

    /**
     * @param canRelease true if the test batch can be released, otherwise false.
     * @param readyCount The number of donations ready to be released.
     */
    public CanReleaseResult(boolean canRelease, int readyCount) {
      this.canRelease = canRelease;
      this.readyCount = readyCount;
    }

    public int getReadyCount() {
      return readyCount;
    }

    public void setReadyCount(int readyCount) {
      this.readyCount = readyCount;
    }

    public boolean canRelease() {
      return canRelease;
    }

    public void setCanRelease(boolean canRelease) {
      this.canRelease = canRelease;
    }
  }
}

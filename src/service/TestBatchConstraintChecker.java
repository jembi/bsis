package service;

import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import viewmodel.BloodTestingRuleResult;

@Service
public class TestBatchConstraintChecker {
    
    @Autowired
    private DonationConstraintChecker donationConstraintChecker;
    @Autowired
    private BloodTestsService bloodTestsService;

    /**
     * A test batch can be released if it is open and none of the donations have outstanding outcomes.
     */
    public CanReleaseResult canReleaseTestBatch(TestBatch testBatch) {
      
        if (testBatch.getStatus() != TestBatchStatus.OPEN) {
            // Only open test batches can be released
            return new CanReleaseResult(false);
        }
        
        int readyCount = 0;
        int totalCount = 0;

        // Check for tests with outstanding test outcomes
        if (testBatch.getDonationBatches() != null) {

	        for (DonationBatch donationBatch : testBatch.getDonationBatches()) {
	
	            for (Donation donation : donationBatch.getDonations()) {
	              
	                if (!donation.getPackType().getTestSampleProduced()) {
	                    // Don't consider donations without test samples
	                    continue;
	                }
	              
	                // Count this donation in the total
	                totalCount++;
	                
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
	        }
        }

        return new CanReleaseResult(true, readyCount, totalCount);
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
	 * Donation Batches can be added or removed from a Test Batch as long as there aren't any test
	 * results recorded.
	 */
	public boolean canAddOrRemoveDonationBatch(TestBatch testBatch) {
		return canEditTestBatch(testBatch) && !testBatchHasResults(testBatch);
	}
    
    /**
     * A test batch can be closed if it is released and none of the donations have discrepancies.
     */
    public boolean canCloseTestBatch(TestBatch testBatch) {

        if (testBatch.getStatus() != TestBatchStatus.RELEASED) {
            // Only released batches can be closed
            return false;
        }
        
        if (testBatch.getDonationBatches() != null) {
	        for (DonationBatch donationBatch : testBatch.getDonationBatches()) {
	
	            for (Donation donation : donationBatch.getDonations()) {
	
	                if (donationConstraintChecker.donationHasDiscrepancies(donation)) {
	                    
	                    // This donation has discrepancies
	                    return false;
	                }
	            }
	        }
        }

        return true;
    }

    protected boolean testBatchHasResults(TestBatch testBatch) {
		if (testBatch.getDonationBatches() != null) {
			for (DonationBatch donationBatch : testBatch.getDonationBatches()) {
				for (Donation donation : donationBatch.getDonations()) {
					if (donationConstraintChecker.donationHasSavedTestResults(donation)) {
						// test results have been recorded for this donation
						return true;
					}
				}
			}
		}
		return false;
    }
  
  public static class CanReleaseResult {
    
    private boolean canRelease;
    private int readyCount;
    private int totalCount;

    /**
     * @param canRelease true if the test batch can be released, otherwise false.
     */
    public CanReleaseResult(boolean canRelease) {
      this.canRelease = canRelease;
    }

    /**
     * @param canRelease true if the test batch can be released, otherwise false.
     * @param readyCount The number of donations ready to be released.
     * @param totalCount The total number of donations.
     */
    public CanReleaseResult(boolean canRelease, int readyCount, int totalCount) {
      this.canRelease = canRelease;
      this.readyCount = readyCount;
      this.totalCount = totalCount;
    }

    public int getReadyCount() {
      return readyCount;
    }

    public void setReadyCount(int readyCount) {
      this.readyCount = readyCount;
    }

    public int getTotalCount() {
      return totalCount;
    }

    public void setTotalCount(int totalCount) {
      this.totalCount = totalCount;
    }

    public boolean canRelease() {
      return canRelease;
    }

    public void setCanRelease(boolean canRelease) {
      this.canRelease = canRelease;
    }
  }
}

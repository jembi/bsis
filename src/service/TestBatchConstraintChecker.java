package service;

import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestBatchConstraintChecker {
    
    @Autowired
    private DonationConstraintChecker donationConstraintChecker;

    /**
     * A test batch can be released if it is open and none of the donations have outstanding outcomes.
     */
    public boolean canReleaseTestBatch(TestBatch testBatch) {

        if (testBatch.getStatus() != TestBatchStatus.OPEN) {
            // Only open test batches can be released
            return false;
        }

        // Check for tests with outstanding test outcomes
        for (DonationBatch donationBatch : testBatch.getDonationBatches()) {

            for (Donation donation : donationBatch.getDonations()) {
                
                if (donationConstraintChecker.donationHasOutstandingOutcomes(donation)) {
                    
                    // This test has an outstanding outcome
                    return false;
                }
            }
        }

        return true;
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
        
        for (DonationBatch donationBatch : testBatch.getDonationBatches()) {

            for (Donation donation : donationBatch.getDonations()) {

                if (donationConstraintChecker.donationHasDiscrepancies(donation)) {
                    
                    // This donation has discrepancies
                    return false;
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
}

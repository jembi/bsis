package service;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.BloodTestType;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.springframework.stereotype.Service;

@Service
public class TestBatchConstraintChecker {

    public boolean canReleaseTestBatch(TestBatch testBatch) {

        if (testBatch.getStatus() != TestBatchStatus.OPEN) {
            // Only open test batches can be released
            return false;
        }

        // Check for tests with outstanding test outcomes
        for (DonationBatch donationBatch : testBatch.getDonationBatches()) {

            for (Donation donation : donationBatch.getDonations()) {

                for (BloodTestResult bloodTestResult : donation.getBloodTestResults()) {

                    BloodTest bloodTest = bloodTestResult.getBloodTest();

                    if ((bloodTestResult.getResult() == null || bloodTestResult.getResult().isEmpty()) &&
                            (bloodTest.getBloodTestType() == BloodTestType.BASIC_BLOODTYPING ||
                            bloodTest.getBloodTestType() == BloodTestType.BASIC_TTI)) {

                        // This test has an outstanding outcome
                        return false;
                    }
                }
            }
        }

        return true;
    }

}

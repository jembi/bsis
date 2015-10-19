package service;

import static helpers.builders.BloodTestBuilder.aBloodTest;
import static helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static helpers.builders.DonationBatchBuilder.aDonationBatch;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.TestBatchBuilder.aTestBatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import java.util.Arrays;
import java.util.List;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.BloodTestType;
import model.donation.Donation;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.junit.Test;
import org.mockito.InjectMocks;
import suites.UnitTestSuite;

public class TestBatchConstraintCheckerTests extends UnitTestSuite {
    
    @InjectMocks
    private TestBatchConstraintChecker testBatchConstraintChecker;

    @Test
    public void testCanReleaseTestBatchWithNonOpenTestBatch_shouldReturnFalse() {
        
        TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.CLOSED).build();
        
        boolean result = testBatchConstraintChecker.canReleaseTestBatch(testBatch);
        
        assertThat(result, is(false));
    }

    @Test
    public void testCanReleaseTestBatchWithNoOutstandingTestResults_shouldReturnTrue() {
        
        List<BloodTestResult> bloodTestResults = Arrays.asList(
                aBloodTestResult()
                        .withBloodTest(aBloodTest().withBloodTestType(BloodTestType.BASIC_BLOODTYPING).build())
                        .withResult("POS")
                        .build(),
                aBloodTestResult()
                        .withBloodTest(aBloodTest().withBloodTestType(BloodTestType.BASIC_TTI).build())
                        .withResult("NEG")
                        .build(),
                aBloodTestResult()
                        .withBloodTest(aBloodTest().withBloodTestType(BloodTestType.CONFIRMATORY_TTI).build())
                        .withResult(null)
                        .build(),
                aBloodTestResult()
                        .withBloodTest(aBloodTest().withBloodTestType(BloodTestType.ADVANCED_BLOODTYPING).build())
                        .withResult("")
                        .build()
        );
        
        List<Donation> donations = Arrays.asList(aDonation().withBloodTestResults(bloodTestResults).build());
        
        TestBatch testBatch = aTestBatch()
                .withStatus(TestBatchStatus.OPEN)
                .withDonationBatches(Arrays.asList(aDonationBatch().withDonations(donations).build()))
                .build();
        
        boolean result = testBatchConstraintChecker.canReleaseTestBatch(testBatch);
        
        assertThat(result, is(true));
    }

    @Test
    public void testCanReleaseTestBatchWithOutstandingBloodTypingTestResults_shouldReturnFalse() {
        
        List<BloodTestResult> bloodTestResults = Arrays.asList(
                aBloodTestResult()
                        .withBloodTest(aBloodTest().withBloodTestType(BloodTestType.BASIC_BLOODTYPING).build())
                        .withResult(null)
                        .build()
        );
        
        List<Donation> donations = Arrays.asList(aDonation().withBloodTestResults(bloodTestResults).build());
        
        TestBatch testBatch = aTestBatch()
                .withStatus(TestBatchStatus.OPEN)
                .withDonationBatches(Arrays.asList(aDonationBatch().withDonations(donations).build()))
                .build();
        
        boolean result = testBatchConstraintChecker.canReleaseTestBatch(testBatch);
        
        assertThat(result, is(false));
    }

    @Test
    public void testCanReleaseTestBatchWithOutstandingTTITestResults_shouldReturnFalse() {
        
        List<BloodTestResult> bloodTestResults = Arrays.asList(
                aBloodTestResult()
                        .withBloodTest(aBloodTest().withBloodTestType(BloodTestType.BASIC_TTI).build())
                        .withResult(null)
                        .build()
        );
        
        List<Donation> donations = Arrays.asList(aDonation().withBloodTestResults(bloodTestResults).build());
        
        TestBatch testBatch = aTestBatch()
                .withStatus(TestBatchStatus.OPEN)
                .withDonationBatches(Arrays.asList(aDonationBatch().withDonations(donations).build()))
                .build();
        
        boolean result = testBatchConstraintChecker.canReleaseTestBatch(testBatch);
        
        assertThat(result, is(false));
    }
}

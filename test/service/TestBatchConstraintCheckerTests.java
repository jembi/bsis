package service;

import static helpers.builders.BloodTestBuilder.aBloodTest;
import static helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static helpers.builders.DonationBatchBuilder.aDonationBatch;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.TestBatchBuilder.aTestBatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.BloodTestType;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import suites.UnitTestSuite;

public class TestBatchConstraintCheckerTests extends UnitTestSuite {
    
    @InjectMocks
    private TestBatchConstraintChecker testBatchConstraintChecker;
    @Mock
    private DonationConstraintChecker donationConstraintChecker;

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
    
    @Test
    public void testCanCloseTestBatchWithOpenTestBatch_shouldReturnFalse() {
        
        TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.OPEN).build();
        
        boolean result = testBatchConstraintChecker.canCloseTestBatch(testBatch);
        
        assertThat(result, is(false));
    }
    
    @Test
    public void testCanCloseTestBatchWithNoDonationBatches_shouldReturnTrue() {
        
        TestBatch testBatch = aTestBatch()
                .withStatus(TestBatchStatus.RELEASED)
                .withDonationBatches(Collections.<DonationBatch>emptyList())
                .build();
        
        boolean result = testBatchConstraintChecker.canCloseTestBatch(testBatch);
        
        assertThat(result, is(true));
    }
    
    @Test
    public void testCanCloseTestBatchWithNoDonations_shouldReturnTrue() {
        
        TestBatch testBatch = aTestBatch()
                .withStatus(TestBatchStatus.RELEASED)
                .withDonationBatch(aDonationBatch().withDonations(Collections.<Donation>emptyList()).build())
                .build();
        
        boolean result = testBatchConstraintChecker.canCloseTestBatch(testBatch);
        
        assertThat(result, is(true));
    }
    
    @Test
    public void testCanCloseTestBatchWithDonationWithoutDiscrepancies_shouldReturnTrue() {
        
        Donation donationWithoutDiscrepancies = aDonation().build();
        TestBatch testBatch = aTestBatch()
                .withStatus(TestBatchStatus.RELEASED)
                .withDonationBatch(aDonationBatch().withDonation(donationWithoutDiscrepancies).build())
                .build();
        
        when(donationConstraintChecker.donationHasDiscrepancies(donationWithoutDiscrepancies)).thenReturn(false);
        
        boolean result = testBatchConstraintChecker.canCloseTestBatch(testBatch);
        
        assertThat(result, is(true));
    }
    
    @Test
    public void testCanCloseTestBatchWithDonationWithDiscrepancies_shouldReturnFalse() {
        
        Donation donationWithDiscrepancies = aDonation().build();
        TestBatch testBatch = aTestBatch()
                .withStatus(TestBatchStatus.RELEASED)
                .withDonationBatch(aDonationBatch().withDonation(donationWithDiscrepancies).build())
                .build();
        
        when(donationConstraintChecker.donationHasDiscrepancies(donationWithDiscrepancies)).thenReturn(true);
        
        boolean result = testBatchConstraintChecker.canCloseTestBatch(testBatch);
        
        assertThat(result, is(false));
    }
}

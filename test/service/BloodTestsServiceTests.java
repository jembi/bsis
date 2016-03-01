package service;

import static helpers.builders.BloodTestingRuleResultBuilder.aBloodTestingRuleResult;
import static helpers.builders.DonationBatchBuilder.aDonationBatch;
import static helpers.builders.DonationBuilder.aDonation;
import static helpers.builders.TestBatchBuilder.aTestBatch;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import constant.GeneralConfigConstants;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;
import repository.DonationRepository;
import repository.bloodtesting.BloodTestingRepository;
import repository.bloodtesting.BloodTestingRuleEngine;
import suites.UnitTestSuite;
import viewmodel.BloodTestingRuleResult;

public class BloodTestsServiceTests extends UnitTestSuite {
  
  private static final Long IRRELEVANT_DONATION_ID = 111L;
  
  @InjectMocks
  private BloodTestsService bloodTestsService;
  @Mock
  private DonationRepository donationRepository;
  @Mock
  private GeneralConfigAccessorService generalConfigAccessorService;
  @Mock
  private BloodTestingRuleEngine bloodTestingRuleEngine;
  @Mock
  private BloodTestingRepository bloodTestingRepository;
  
  @Test
  public void testSaveBloodTestsWithReEntryRequired_shouldApplyAndSaveBloodTestsAsReEntry() {
    
    // Set up fixture
    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.OPEN).build();
    DonationBatch donationBatch = aDonationBatch().withTestBatch(testBatch).build();
    Donation donation = aDonation().withId(IRRELEVANT_DONATION_ID).withDonationBatch(donationBatch).build();
    Map<Long, String> bloodTestResults = new HashMap<>();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();
    
    // Set up expectations
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donation);
    when(bloodTestingRuleEngine.applyBloodTests(donation, bloodTestResults)).thenReturn(bloodTestingRuleResult);
    
    // Exercise SUT
    BloodTestingRuleResult returnedBloodTestingRuleResult = bloodTestsService.saveBloodTests(IRRELEVANT_DONATION_ID,
        bloodTestResults, true);
    
    // Verify
    verify(bloodTestingRuleEngine, times(2)).applyBloodTests(donation, bloodTestResults);
    verify(bloodTestingRepository, times(2)).saveBloodTestResultsToDatabase(eq(bloodTestResults), eq(donation),
        any(Date.class), eq(bloodTestingRuleResult), eq(true));
    assertThat(returnedBloodTestingRuleResult, is(bloodTestingRuleResult));
  }
  
  @Test
  public void testSaveBloodTestsWithReEntryNotRequiredAndNoConfig_shouldApplyAndSaveBloodTestsAsNotReEntry() {
    
    // Set up fixture
    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.OPEN).build();
    DonationBatch donationBatch = aDonationBatch().withTestBatch(testBatch).build();
    Donation donation = aDonation().withId(IRRELEVANT_DONATION_ID).withDonationBatch(donationBatch).build();
    Map<Long, String> bloodTestResults = new HashMap<>();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();
    
    // Set up expectations
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.TESTING_RE_ENTRY_REQUIRED, true)).thenReturn(true);
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donation);
    when(bloodTestingRuleEngine.applyBloodTests(donation, bloodTestResults)).thenReturn(bloodTestingRuleResult);
    
    // Exercise SUT
    BloodTestingRuleResult returnedBloodTestingRuleResult = bloodTestsService.saveBloodTests(IRRELEVANT_DONATION_ID,
        bloodTestResults, false);
    
    // Verify
    verify(bloodTestingRuleEngine, times(2)).applyBloodTests(donation, bloodTestResults);
    verify(bloodTestingRepository, times(2)).saveBloodTestResultsToDatabase(eq(bloodTestResults), eq(donation),
        any(Date.class), eq(bloodTestingRuleResult), eq(false));
    assertThat(returnedBloodTestingRuleResult, is(bloodTestingRuleResult));
  }
  
  @Test
  public void testSaveBloodTestsWithReEntryNotRequiredAndConfigSetToFalse_shouldApplyAndSaveBloodTestsAsNotReEntry() {
    
    // Set up fixture
    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.OPEN).build();
    DonationBatch donationBatch = aDonationBatch().withTestBatch(testBatch).build();
    Donation donation = aDonation().withId(IRRELEVANT_DONATION_ID).withDonationBatch(donationBatch).build();
    Map<Long, String> bloodTestResults = new HashMap<>();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();
    
    // Set up expectations
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.TESTING_RE_ENTRY_REQUIRED, true)).thenReturn(false);
    when(donationRepository.findDonationById(IRRELEVANT_DONATION_ID)).thenReturn(donation);
    when(bloodTestingRuleEngine.applyBloodTests(donation, bloodTestResults)).thenReturn(bloodTestingRuleResult);
    
    // Exercise SUT
    BloodTestingRuleResult returnedBloodTestingRuleResult = bloodTestsService.saveBloodTests(IRRELEVANT_DONATION_ID,
        bloodTestResults, false);
    
    // Verify
    verify(bloodTestingRuleEngine, times(2)).applyBloodTests(donation, bloodTestResults);
    verify(bloodTestingRepository, times(2)).saveBloodTestResultsToDatabase(eq(bloodTestResults), eq(donation),
        any(Date.class), eq(bloodTestingRuleResult), eq(true));
    assertThat(returnedBloodTestingRuleResult, is(bloodTestingRuleResult));
  }

}

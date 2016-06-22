package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleResultBuilder.aBloodTestingRuleResult;
import static org.jembi.bsis.helpers.builders.DonationBatchBuilder.aDonationBatch;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.builders.TestBatchBuilder.aTestBatch;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.jembi.bsis.backingform.TestResultsBackingForm;
import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.helpers.builders.BloodTestBuilder;
import org.jembi.bsis.helpers.builders.TestResultsBackingFormBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRuleEngine;
import org.jembi.bsis.service.BloodTestsService;
import org.jembi.bsis.service.GeneralConfigAccessorService;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class BloodTestsServiceTests extends UnitTestSuite {
  
  private static final String IRRELEVANT_DONATION_DIN = "1234567";
  
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
    Donation donation = aDonation().withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN)
        .withDonationBatch(donationBatch).build();
    Map<Long, String> bloodTestResults = new HashMap<>();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();
    
    // Set up expectations
    when(donationRepository.findDonationByDonationIdentificationNumber(IRRELEVANT_DONATION_DIN)).thenReturn(donation);
    when(bloodTestingRuleEngine.applyBloodTests(donation, bloodTestResults)).thenReturn(bloodTestingRuleResult);
    
    // Exercise SUT
    TestResultsBackingForm form = TestResultsBackingFormBuilder.aTestResultsBackingForm()
        .withDonationIdentificationNumber(donation.getDonationIdentificationNumber())
        .withTestResults(bloodTestResults)
        .build();
    ArrayList<TestResultsBackingForm> forms = new ArrayList<>();
    forms.add(form);
    bloodTestsService.saveBloodTests(forms, true);
    
    // Verify
    verify(bloodTestingRuleEngine, times(2)).applyBloodTests(donation, bloodTestResults);
    verify(bloodTestingRepository, times(2)).saveBloodTestResultsToDatabase(eq(bloodTestResults), eq(donation),
        any(Date.class), eq(bloodTestingRuleResult), eq(true));
  }
  
  @Test
  public void testSaveBloodTestsWithReEntryNotRequiredAndNoConfig_shouldApplyAndSaveBloodTestsAsNotReEntry() {
    
    // Set up fixture
    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.OPEN).build();
    DonationBatch donationBatch = aDonationBatch().withTestBatch(testBatch).build();
    Donation donation = aDonation().withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN).withDonationBatch(donationBatch).build();
    Map<Long, String> bloodTestResults = new HashMap<>();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();
    
    // Set up expectations
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.TESTING_RE_ENTRY_REQUIRED, true)).thenReturn(true);
    when(donationRepository.findDonationByDonationIdentificationNumber(IRRELEVANT_DONATION_DIN)).thenReturn(donation);
    when(bloodTestingRuleEngine.applyBloodTests(donation, bloodTestResults)).thenReturn(bloodTestingRuleResult);
    
    // Exercise SUT
    TestResultsBackingForm form = TestResultsBackingFormBuilder.aTestResultsBackingForm()
        .withDonationIdentificationNumber(donation.getDonationIdentificationNumber())
        .withTestResults(bloodTestResults)
        .build();
    ArrayList<TestResultsBackingForm> forms = new ArrayList<>();
    forms.add(form);
    bloodTestsService.saveBloodTests(forms, false);
    
    // Verify
    verify(bloodTestingRuleEngine, times(2)).applyBloodTests(donation, bloodTestResults);
    verify(bloodTestingRepository, times(2)).saveBloodTestResultsToDatabase(eq(bloodTestResults), eq(donation),
        any(Date.class), eq(bloodTestingRuleResult), eq(false));
  }
  
  @Test
  public void testSaveBloodTestsWithReEntryNotRequiredAndConfigSetToFalse_shouldApplyAndSaveBloodTestsAsNotReEntry() {
    
    // Set up fixture
    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.OPEN).build();
    DonationBatch donationBatch = aDonationBatch().withTestBatch(testBatch).build();
    Donation donation = aDonation().withDonationBatch(donationBatch)
        .withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN).build();
    Map<Long, String> bloodTestResults = new HashMap<>();
    BloodTestingRuleResult bloodTestingRuleResult = aBloodTestingRuleResult().build();
    
    // Set up expectations
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.TESTING_RE_ENTRY_REQUIRED, true)).thenReturn(false);
    when(donationRepository.findDonationByDonationIdentificationNumber(IRRELEVANT_DONATION_DIN)).thenReturn(donation);
    when(bloodTestingRuleEngine.applyBloodTests(donation, bloodTestResults)).thenReturn(bloodTestingRuleResult);
    
    // Exercise SUT
    TestResultsBackingForm form = TestResultsBackingFormBuilder.aTestResultsBackingForm()
        .withDonationIdentificationNumber(donation.getDonationIdentificationNumber())
        .withTestResults(bloodTestResults)
        .build();
    ArrayList<TestResultsBackingForm> forms = new ArrayList<>();
    forms.add(form);
    bloodTestsService.saveBloodTests(forms, false);
    
    // Verify
    verify(bloodTestingRuleEngine, times(2)).applyBloodTests(donation, bloodTestResults);
    verify(bloodTestingRepository, times(2)).saveBloodTestResultsToDatabase(eq(bloodTestResults), eq(donation),
        any(Date.class), eq(bloodTestingRuleResult), eq(true));
  }

  @Test
  public void testAddTestNamesToMap() {

    BloodTest hiv =
        BloodTestBuilder.aBloodTest().withBloodTestType(BloodTestType.BASIC_TTI).withTestNameShort("HIV").build();
    when(bloodTestingRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI)).thenReturn(Arrays.asList(hiv));
    Map<String, Object> map = bloodTestsService.getBloodTestShortNames();
    assertThat(((ArrayList<String>) map.get("basicTtiTestNames")), is(Arrays.asList("HIV")));
  }

}

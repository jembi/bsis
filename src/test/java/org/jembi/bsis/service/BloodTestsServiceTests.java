package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRepository;
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
  private BloodTestRepository bloodTestRepository;
  @Mock
  private BloodTestingRepository bloodTestingRepository;
  
  @Test
  public void testSaveBloodTestsWithReEntryRequired_shouldApplyAndSaveBloodTestsAsReEntry() {
    
    // Set up fixture
    TestBatch testBatch = aTestBatch().withStatus(TestBatchStatus.OPEN).build();
    DonationBatch donationBatch = aDonationBatch().build();
    Donation donation = aDonation().withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN)
        .withDonationBatch(donationBatch).withTestBatch(testBatch).build();
    Map<UUID, String> bloodTestResults = new HashMap<>();
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
    DonationBatch donationBatch = aDonationBatch().build();
    Donation donation = aDonation().withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN)
        .withDonationBatch(donationBatch).withTestBatch(testBatch).build();
    Map<UUID, String> bloodTestResults = new HashMap<>();
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
    DonationBatch donationBatch = aDonationBatch().build();
    Donation donation = aDonation().withDonationBatch(donationBatch).withTestBatch(testBatch)
        .withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN).build();
    Map<UUID, String> bloodTestResults = new HashMap<>();
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

  @SuppressWarnings("unchecked")
  @Test
  public void testAddTestNamesToMap() {

    BloodTest hiv = BloodTestBuilder.aBloodTest()
        .withBloodTestType(BloodTestType.BASIC_TTI).withTestNameShort("HIV").build();
    BloodTest hivRepeat = BloodTestBuilder.aBloodTest()
        .withBloodTestType(BloodTestType.REPEAT_TTI).withTestNameShort("HIV_REPEAT").build();
    BloodTest hivConf = BloodTestBuilder.aBloodTest()
        .withBloodTestType(BloodTestType.CONFIRMATORY_TTI).withTestNameShort("HIV_CONF").build();
    BloodTest abo = BloodTestBuilder.aBloodTest()
        .withBloodTestType(BloodTestType.BASIC_BLOODTYPING).withTestNameShort("ABO").build();
    BloodTest aboRepeat = BloodTestBuilder.aBloodTest()
        .withBloodTestType(BloodTestType.REPEAT_BLOODTYPING).withTestNameShort("ABO_REPEAT").build();
    
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_TTI)).thenReturn(Arrays.asList(hiv));
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.REPEAT_TTI)).thenReturn(Arrays.asList(hivRepeat));
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.CONFIRMATORY_TTI)).thenReturn(Arrays.asList(hivConf));
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING)).thenReturn(Arrays.asList(abo));
    when(bloodTestRepository.getBloodTestsOfType(BloodTestType.REPEAT_BLOODTYPING)).thenReturn(Arrays.asList(aboRepeat));
    
    Map<String, Object> map = bloodTestsService.getBloodTestShortNames();
    
    assertThat(((ArrayList<String>) map.get("basicTtiTestNames")), is(Arrays.asList("HIV")));
    assertThat(((ArrayList<String>) map.get("repeatTtiTestNames")), is(Arrays.asList("HIV_REPEAT")));
    assertThat(((ArrayList<String>) map.get("confirmatoryTtiTestNames")), is(Arrays.asList("HIV_CONF")));
    assertThat(((ArrayList<String>) map.get("basicBloodTypingTestNames")), is(Arrays.asList("ABO")));
    assertThat(((ArrayList<String>) map.get("repeatBloodTypingTestNames")), is(Arrays.asList("ABO_REPEAT")));
  }


  @Test
  public void testExecuteTests_shouldCallRulesEngine() throws Exception {
    Donation donation = aDonation().withId(UUID.randomUUID()).build();
    BloodTestingRuleResult expectedResult = aBloodTestingRuleResult().build();

    when(bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>())).thenReturn(expectedResult);

    BloodTestingRuleResult result = bloodTestsService.executeTests(donation);

    assertThat(result, is(expectedResult));
  }

  @Test
  public void testExecuteTestsForDonationList_shouldCallRulesEngineForEachDonation() throws Exception {
    Donation donation1 = aDonation().withId(UUID.randomUUID()).build();
    Donation donation2 = aDonation().withId(UUID.randomUUID()).build();
    Donation donation3 = aDonation().withId(UUID.randomUUID()).build();

    BloodTestingRuleResult expectedResult1 = aBloodTestingRuleResult().build();
    BloodTestingRuleResult expectedResult2 = aBloodTestingRuleResult().build();
    BloodTestingRuleResult expectedResult3 = aBloodTestingRuleResult().build();

    when(bloodTestingRuleEngine.applyBloodTests(donation1, new HashMap<UUID, String>())).thenReturn(expectedResult1);
    when(bloodTestingRuleEngine.applyBloodTests(donation2, new HashMap<UUID, String>())).thenReturn(expectedResult2);
    when(bloodTestingRuleEngine.applyBloodTests(donation3, new HashMap<UUID, String>())).thenReturn(expectedResult3);

    List<BloodTestingRuleResult> results = bloodTestsService.executeTests(Arrays.asList(donation1, donation2, donation3));

    assertThat(results, hasItem(expectedResult1));
    assertThat(results, hasItem(expectedResult2));
    assertThat(results, hasItem(expectedResult3));
  }
}

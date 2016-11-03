package org.jembi.bsis.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jembi.bsis.backingform.TestResultsBackingForm;
import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.helpers.builders.BloodTestBuilder;
import org.jembi.bsis.helpers.builders.BloodTestResultBuilder;
import org.jembi.bsis.helpers.builders.BloodTestingRuleResultBuilder;
import org.jembi.bsis.helpers.builders.DonationBatchBuilder;
import org.jembi.bsis.helpers.builders.DonationBuilder;
import org.jembi.bsis.helpers.builders.TestBatchBuilder;
import org.jembi.bsis.helpers.builders.TestResultsBackingFormBuilder;
import org.jembi.bsis.helpers.matchers.BloodTestResultMatcher;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.TTIStatus;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donationbatch.DonationBatch;
import org.jembi.bsis.model.testbatch.TestBatch;
import org.jembi.bsis.model.testbatch.TestBatchStatus;
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.repository.BloodTestResultRepository;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

public class BloodTestsServiceTest extends UnitTestSuite {

  private static final String IRRELEVANT_DONATION_DIN_1 = "1111111";
  private static final String IRRELEVANT_DONATION_DIN_2 = "2222222";

  @InjectMocks
  BloodTestsService service;

  @InjectMocks
  BloodTestingRepository bloodTestingRepository;

  @Mock
  BloodTestingRuleEngine ruleEngine;

  @Mock
  DonationRepository donationRepository;

  @Mock
  EntityManager entityManager;

  @Mock
  TypedQuery typedQuery;

  @Mock
  TestBatchStatusChangeService testBatchStatusChangeService;

  @Mock
  private BloodTestResultRepository bloodTestResultRepository;

  @Mock
  private GeneralConfigAccessorService generalConfigAccessorService;
  
  @Mock
  private BloodTestRepository bloodTestRepository;

  @Before
  public void setup() {
    service.setBloodTestingRepository(bloodTestingRepository);
  }

  @Test
  public void testUpdateDonationWithTestResultsNotUpdated() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation().withId(1l).withBloodAbo("A").withBloodRh("+")
        .withTTIStatus(TTIStatus.TTI_SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withExtraBloodTypeInformation("").build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    ruleResult.setTTIStatus(TTIStatus.TTI_SAFE);
    ruleResult.setExtraInformation(new HashSet<String>());

    // set up mocks

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    Assert.assertFalse("Donation updated", updated);
  }

  @Test
  public void testUpdateDonationWithTestResultsUpdatedBloodAbo() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation().withId(1l).withBloodRh("+").withTTIStatus(TTIStatus.NOT_DONE)
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE).withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.NOT_DONE);
    ruleResult.setExtraInformation(new HashSet<String>());

    // set up mocks

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    Assert.assertTrue("Donation updated", updated);
    Assert.assertEquals("BloodAbo set", "A", donation.getBloodAbo());
  }

  public void testUpdateDonationWithTestResultsUpdatedBloodRh() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation().withId(1l).withBloodAbo("A").withTTIStatus(TTIStatus.NOT_DONE)
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE).withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.NOT_DONE);
    ruleResult.setExtraInformation(new HashSet<String>());

    // set up mocks

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    Assert.assertTrue("Donation updated", updated);
    Assert.assertEquals("BloodRh set", "+", donation.getBloodRh());
  }

  @Test
  public void testUpdateDonationWithTestResultsUpdatedTTISafe() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation().withId(1l).withBloodAbo("A").withBloodRh("+")
        .withTTIStatus(TTIStatus.NOT_DONE).withBloodTypingStatus(BloodTypingStatus.NOT_DONE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE).build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.TTI_SAFE);
    ruleResult.setExtraInformation(new HashSet<String>());

    // set up mocks

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    Assert.assertTrue("Donation updated", updated);
    Assert.assertEquals("TTI status set", TTIStatus.TTI_SAFE, donation.getTTIStatus());
  }

  @Test
  public void testUpdateDonationWithTestResultsUpdatedBloodTypingStatus() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation().withId(1l).withBloodAbo("A").withBloodRh("+")
        .withTTIStatus(TTIStatus.TTI_SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE).build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.PENDING_TESTS);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.TTI_SAFE);
    ruleResult.setExtraInformation(new HashSet<String>());

    // set up mocks

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    Assert.assertTrue("Donation updated", updated);
    Assert.assertEquals("BloodTypingStatus set", BloodTypingStatus.PENDING_TESTS, donation.getBloodTypingStatus());
  }

  @Test
  public void testUpdateDonationWithTestResultsUpdatedBloodTypingMatchStatus() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation().withId(1l).withBloodAbo("A").withBloodRh("+")
        .withTTIStatus(TTIStatus.TTI_SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE).build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    ruleResult.setTTIStatus(TTIStatus.TTI_SAFE);
    ruleResult.setExtraInformation(new HashSet<String>());

    // set up mocks

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    Assert.assertTrue("Donation updated", updated);
    Assert.assertEquals("BloodTypingMatchStatus set", BloodTypingMatchStatus.MATCH,
        donation.getBloodTypingMatchStatus());
  }

  @Test
  public void testUpdateDonationWithTestResultsUpdatedNotTTISafe() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation().withId(1l).withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE).withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    ruleResult.setTTIStatus(TTIStatus.TTI_UNSAFE);
    ruleResult.setExtraInformation(new HashSet<String>());

    // set up mocks

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    Assert.assertTrue("Donation updated", updated);
    Assert.assertEquals("TTI status set", TTIStatus.TTI_UNSAFE, donation.getTTIStatus());
  }

  @Test
  public void testAddNewExtraInformationNone() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation().withId(1l).withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE).withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withExtraBloodTypeInformation(null).build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    ruleResult.setTTIStatus(TTIStatus.TTI_UNSAFE);
    Set<String> newExtraInformation = new HashSet<>();
    newExtraInformation.add("test1");
    ruleResult.setExtraInformation(newExtraInformation);

    // set up mocks

    // run test
    service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    String updatedExtraInformation = donation.getExtraBloodTypeInformation();
    Assert.assertEquals("Extra information set correctly", "test1", updatedExtraInformation);
  }

  @Test
  public void testAddNewExtraInformationOne() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation().withId(1l).withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE).withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withExtraBloodTypeInformation("test2").build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    ruleResult.setTTIStatus(TTIStatus.TTI_UNSAFE);
    Set<String> newExtraInformation = new HashSet<>();
    ruleResult.setExtraInformation(newExtraInformation);

    // set up mocks

    // run test
    service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    String updatedExtraInformation = donation.getExtraBloodTypeInformation();
    Assert.assertEquals("Extra information set correctly", "test2", updatedExtraInformation);
  }

  @Test
  public void testAddNewExtraInformationTwo() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation().withId(1l).withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE).withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withExtraBloodTypeInformation("test2").build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    ruleResult.setTTIStatus(TTIStatus.TTI_UNSAFE);
    Set<String> newExtraInformation = new HashSet<>();
    newExtraInformation.add("test1");
    ruleResult.setExtraInformation(newExtraInformation);

    // set up mocks

    // run test
    service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    String updatedExtraInformation = donation.getExtraBloodTypeInformation();
    Assert.assertEquals("Extra information set correctly", "test2test1", updatedExtraInformation);
  }

  @Test
  public void testAddNewExtraInformationThree() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation().withId(1l).withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE).withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withExtraBloodTypeInformation("test3").build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    ruleResult.setTTIStatus(TTIStatus.TTI_UNSAFE);
    Set<String> newExtraInformation = new HashSet<>();
    newExtraInformation.add("test1");
    newExtraInformation.add("test2");
    ruleResult.setExtraInformation(newExtraInformation);

    // set up mocks

    // run test
    service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    String updatedExtraInformation = donation.getExtraBloodTypeInformation();
    Assert.assertTrue("Extra information set correctly",
        "test3test1,test2".equals(updatedExtraInformation) || "test3test2,test1".equals(updatedExtraInformation));
  }

  @Test
  public void testSaveBloodTestingResultsTestBatchNotReleased() throws Exception {
    // set up data
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.OPEN).build();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().withTestBatch(testBatch).build();
    Donation donation = DonationBuilder.aDonation().withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)
        .withTTIStatus(TTIStatus.TTI_SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withDonationBatch(donationBatch).build();

    Map<Long, String> bloodTestResults = new HashMap<>();
    bloodTestResults.put(1l, "AB");
    BloodTest bloodTest = BloodTestBuilder.aBloodTest().withId(17l).build();
    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(1l).withBloodTest(bloodTest).build());

    BloodTestingRuleResult ruleResult = BloodTestingRuleResultBuilder.aBloodTestingRuleResult().withBloodAbo("AB")
        .withBloodRh("+").withTTIStatus(TTIStatus.TTI_SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withExtraInformation(new HashSet<String>()).build();

    // set up mocks
    when(bloodTestRepository.findBloodTestById(1L)).thenReturn(bloodTest);
    when(donationRepository.findDonationById(donation.getId())).thenReturn(donation);
    when(donationRepository.findDonationByDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)).thenReturn(donation);
    when(ruleEngine.applyBloodTests(donation, bloodTestResults)).thenReturn(ruleResult);
    when(entityManager.createQuery("SELECT bt FROM BloodTestResult bt WHERE "
        + "bt.donation.id=:donationId AND bt.isDeleted = :testOutcomeDeleted", BloodTestResult.class))
            .thenReturn(typedQuery);
    when(typedQuery.setParameter("donationId", 1)).thenReturn(typedQuery);
    when(typedQuery.setParameter("testOutcomeDeleted", false)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(bloodTestResultList);
    when(entityManager.createQuery("SELECT bt FROM BloodTest bt WHERE " + "bt.id=:bloodTestId", BloodTest.class))
        .thenReturn(typedQuery);
    when(typedQuery.setParameter("bloodTestId", 17)).thenReturn(typedQuery);
    when(typedQuery.getSingleResult()).thenReturn(bloodTest);

    // run test
    TestResultsBackingForm form = TestResultsBackingFormBuilder.aTestResultsBackingForm()
        .withDonationIdentificationNumber(donation.getDonationIdentificationNumber()).withTestResults(bloodTestResults)
        .build();
    ArrayList<TestResultsBackingForm> forms = new ArrayList<>();
    forms.add(form);
    service.saveBloodTests(forms, true);

    // check asserts
    Assert.assertEquals("ABO correct", "AB", donation.getBloodAbo());
    verify(testBatchStatusChangeService, never()).handleRelease(donation);
  }

  @Test
  public void testSaveBloodTestingResultsTestBatchReleased() throws Exception {
    // set up data
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.RELEASED).build();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().withTestBatch(testBatch).build();
    Donation donation = DonationBuilder.aDonation().withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)
        .withTTIStatus(TTIStatus.TTI_SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withDonationBatch(donationBatch).build();

    Map<Long, String> bloodTestResults = new HashMap<>();
    bloodTestResults.put(1l, "AB");
    BloodTest bloodTest = BloodTestBuilder.aBloodTest().withId(17l).build();
    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(1l).withBloodTest(bloodTest).build());

    BloodTestingRuleResult ruleResult = BloodTestingRuleResultBuilder.aBloodTestingRuleResult().withBloodAbo("AB")
        .withBloodRh("+").withTTIStatus(TTIStatus.TTI_SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withExtraInformation(new HashSet<String>()).build();

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)).thenReturn(donation);
    when(ruleEngine.applyBloodTests(donation, bloodTestResults)).thenReturn(ruleResult);
    when(entityManager.createQuery("SELECT bt FROM BloodTestResult bt WHERE "
        + "bt.donation.id=:donationId AND bt.isDeleted = :testOutcomeDeleted", BloodTestResult.class))
            .thenReturn(typedQuery);
    when(typedQuery.setParameter("donationId", 1)).thenReturn(typedQuery);
    when(typedQuery.setParameter("testOutcomeDeleted", false)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(bloodTestResultList);
    when(entityManager.createQuery("SELECT bt FROM BloodTest bt WHERE " + "bt.id=:bloodTestId", BloodTest.class))
        .thenReturn(typedQuery);
    when(typedQuery.setParameter("bloodTestId", 17)).thenReturn(typedQuery);
    when(typedQuery.getSingleResult()).thenReturn(bloodTest);

    // run test
    TestResultsBackingForm form = TestResultsBackingFormBuilder.aTestResultsBackingForm()
        .withDonationIdentificationNumber(donation.getDonationIdentificationNumber()).withTestResults(bloodTestResults)
        .build();
    ArrayList<TestResultsBackingForm> forms = new ArrayList<>();
    forms.add(form);
    service.saveBloodTests(forms, true);
    // check asserts
    Assert.assertEquals("ABO correct", "AB", donation.getBloodAbo());
    verify(testBatchStatusChangeService).handleRelease(donation);
  }

  @Test
  public void testSaveBloodTestingResults1stEntry() throws Exception {
    // set up data
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.OPEN).build();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().withTestBatch(testBatch).build();
    Donation donation = DonationBuilder.aDonation().withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)
        .withTTIStatus(TTIStatus.TTI_SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withDonationBatch(donationBatch).build();

    Map<Long, String> bloodTestResults = new HashMap<>(); // tests passed to the 'saveBloodTests'
                                                          // service method
    bloodTestResults.put(1l, "AB");

    Map<Long, String> reEnteredBloodTestResults = new HashMap<>(); // tests passed to the rules
                                                                   // engine

    BloodTest bloodTest = BloodTestBuilder.aBloodTest().withId(17l).build();
    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(1l).withBloodTest(bloodTest).build());

    BloodTestingRuleResult ruleResult = BloodTestingRuleResultBuilder.aBloodTestingRuleResult().withBloodAbo("AB")
        .withBloodRh("+").withTTIStatus(TTIStatus.TTI_SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withExtraInformation(new HashSet<String>()).build();

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)).thenReturn(donation);
    when(ruleEngine.applyBloodTests(donation, reEnteredBloodTestResults)).thenReturn(ruleResult).thenReturn(ruleResult);
    when(entityManager.createQuery("SELECT bt FROM BloodTestResult bt WHERE "
        + "bt.donation.id=:donationId AND bt.isDeleted = :testOutcomeDeleted", BloodTestResult.class))
            .thenReturn(typedQuery);
    when(typedQuery.setParameter("donationId", 1)).thenReturn(typedQuery);
    when(typedQuery.setParameter("testOutcomeDeleted", false)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(bloodTestResultList);
    when(entityManager.createQuery("SELECT bt FROM BloodTest bt WHERE " + "bt.id=:bloodTestId", BloodTest.class))
        .thenReturn(typedQuery);
    when(typedQuery.setParameter("bloodTestId", 17)).thenReturn(typedQuery);
    when(typedQuery.getSingleResult()).thenReturn(bloodTest);
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.TESTING_RE_ENTRY_REQUIRED, true))
        .thenReturn(true);

    // run test
    TestResultsBackingForm form = TestResultsBackingFormBuilder.aTestResultsBackingForm()
        .withDonationIdentificationNumber(donation.getDonationIdentificationNumber()).withTestResults(bloodTestResults)
        .build();
    ArrayList<TestResultsBackingForm> forms = new ArrayList<>();
    forms.add(form);
    service.saveBloodTests(forms, false);

    // check asserts
    verify(ruleEngine, times(2)).applyBloodTests(donation, reEnteredBloodTestResults);
  }

  @Test
  public void testDontAllowDonationReleaseIfReEntryPending() throws Exception {
    // set up data
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.RELEASED).build();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().withTestBatch(testBatch).build();
    Donation donation = DonationBuilder.aDonation().withTTIStatus(TTIStatus.TTI_SAFE)
        .withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withDonationBatch(donationBatch).build();

    Map<Long, String> bloodTestResults = new HashMap<>();
    bloodTestResults.put(1l, "AB");
    BloodTest bloodTest = BloodTestBuilder.aBloodTest().withId(17l).build();
    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(1l).withBloodTest(bloodTest).build());

    BloodTestingRuleResult ruleResult = BloodTestingRuleResultBuilder.aBloodTestingRuleResult().withBloodAbo("AB")
        .withBloodRh("+").withTTIStatus(TTIStatus.TTI_SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withExtraInformation(new HashSet<String>()).build();

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)).thenReturn(donation);
    when(ruleEngine.applyBloodTests(donation, bloodTestResults)).thenReturn(ruleResult);
    when(entityManager.createQuery("SELECT bt FROM BloodTestResult bt WHERE "
        + "bt.donation.id=:donationId AND bt.isDeleted = :testOutcomeDeleted", BloodTestResult.class))
            .thenReturn(typedQuery);
    when(typedQuery.setParameter("donationId", 1)).thenReturn(typedQuery);
    when(typedQuery.setParameter("testOutcomeDeleted", false)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(bloodTestResultList);
    when(entityManager.createQuery("SELECT bt FROM BloodTest bt WHERE " + "bt.id=:bloodTestId", BloodTest.class))
        .thenReturn(typedQuery);
    when(typedQuery.setParameter("bloodTestId", 17)).thenReturn(typedQuery);
    when(typedQuery.getSingleResult()).thenReturn(bloodTest);
    when(generalConfigAccessorService.getBooleanValue(GeneralConfigConstants.TESTING_RE_ENTRY_REQUIRED, true))
        .thenReturn(true);

    // run test with re-entry = false
    TestResultsBackingForm form = TestResultsBackingFormBuilder.aTestResultsBackingForm()
        .withDonationIdentificationNumber(donation.getDonationIdentificationNumber()).withTestResults(bloodTestResults)
        .build();
    ArrayList<TestResultsBackingForm> forms = new ArrayList<>();
    forms.add(form);
    service.saveBloodTests(forms, false);

    verify(testBatchStatusChangeService, never()).handleRelease(donation);

    // run test with re-entry = true
    service.saveBloodTests(forms, true);
    verify(testBatchStatusChangeService).handleRelease(donation);

  }

  @Test
  public void testSaveBloodTestingResultsForListOfDonations() throws Exception {
    // set up data
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.OPEN).build();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().withTestBatch(testBatch).build();
    Donation donation1 = DonationBuilder.aDonation().withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)
        .withTTIStatus(TTIStatus.TTI_SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withDonationBatch(donationBatch).build();
    Donation donation2 = DonationBuilder.aDonation().withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_2)
        .withTTIStatus(TTIStatus.TTI_SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withDonationBatch(donationBatch).build();

    Map<Long, String> bloodTestResults = new HashMap<>();
    bloodTestResults.put(1l, "AB");
    BloodTest bloodTest = BloodTestBuilder.aBloodTest().withId(17l).build();
    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(1l).withBloodTest(bloodTest).build());

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1))
        .thenReturn(donation1);
    when(donationRepository.findDonationByDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_2))
        .thenReturn(donation2);
    when(entityManager.createQuery("SELECT bt FROM BloodTestResult bt WHERE "
        + "bt.donation.id=:donationId AND bt.isDeleted = :testOutcomeDeleted", BloodTestResult.class))
            .thenReturn(typedQuery);
    when(typedQuery.setParameter("donationId", 1)).thenReturn(typedQuery);
    when(typedQuery.setParameter("testOutcomeDeleted", false)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(bloodTestResultList);
    when(entityManager.createQuery("SELECT bt FROM BloodTest bt WHERE " + "bt.id=:bloodTestId", BloodTest.class))
        .thenReturn(typedQuery);
    when(typedQuery.setParameter("bloodTestId", 17)).thenReturn(typedQuery);

    // run test
    TestResultsBackingForm form1 = TestResultsBackingFormBuilder.aTestResultsBackingForm()
        .withDonationIdentificationNumber(donation1.getDonationIdentificationNumber()).withTestResults(bloodTestResults)
        .build();
    TestResultsBackingForm form2 = TestResultsBackingFormBuilder.aTestResultsBackingForm()
        .withDonationIdentificationNumber(donation2.getDonationIdentificationNumber()).withTestResults(bloodTestResults)
        .build();
    ArrayList<TestResultsBackingForm> forms = new ArrayList<>();
    forms.add(form1);
    forms.add(form2);
    service.saveBloodTests(forms, true);

    // check asserts
    verify(ruleEngine, times(2)).applyBloodTests(donation1, bloodTestResults);
    verify(ruleEngine, times(2)).applyBloodTests(donation2, bloodTestResults);

  }

  @Test
  public void testSetTestOutcomesAsDeleted_shouldDeleteAllTestOutcomesForDonation() {
    // Set up data
    Donation donation = DonationBuilder.aDonation().build();
    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(1L).withDonation(donation).build());
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(2L).withDonation(donation).build());
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(3L).withDonation(donation).build());
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(4L).withDonation(donation).build());

    BloodTestResult deleted1 = bloodTestResultList.get(0);
    deleted1.setIsDeleted(true);
    BloodTestResult deleted2 = bloodTestResultList.get(1);
    deleted1.setIsDeleted(true);
    BloodTestResult deleted3 = bloodTestResultList.get(2);
    deleted1.setIsDeleted(true);
    BloodTestResult deleted4 = bloodTestResultList.get(3);
    deleted1.setIsDeleted(true);

    // Mocks
    when(bloodTestResultRepository.getTestOutcomes(donation)).thenReturn(bloodTestResultList);

    // Run test
    service.setTestOutcomesAsDeleted(donation);

    // Verify
    Mockito.verify(bloodTestResultRepository)
        .save(Mockito.argThat(BloodTestResultMatcher.hasSameStateAsBloodTestResult(deleted1)));
    Mockito.verify(bloodTestResultRepository)
        .save(Mockito.argThat(BloodTestResultMatcher.hasSameStateAsBloodTestResult(deleted2)));
    Mockito.verify(bloodTestResultRepository)
        .save(Mockito.argThat(BloodTestResultMatcher.hasSameStateAsBloodTestResult(deleted3)));
    Mockito.verify(bloodTestResultRepository)
        .save(Mockito.argThat(BloodTestResultMatcher.hasSameStateAsBloodTestResult(deleted4)));
  }
}

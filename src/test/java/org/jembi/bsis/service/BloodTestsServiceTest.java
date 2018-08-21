package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.jembi.bsis.helpers.builders.BloodTestingRuleResultBuilder.aBloodTestingRuleResult;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jembi.bsis.backingform.TestResultsBackingForm;
import org.jembi.bsis.constant.GeneralConfigConstants;
import org.jembi.bsis.helpers.builders.BloodTestBuilder;
import org.jembi.bsis.helpers.builders.BloodTestResultBuilder;
import org.jembi.bsis.helpers.builders.DonationBatchBuilder;
import org.jembi.bsis.helpers.builders.TestBatchBuilder;
import org.jembi.bsis.helpers.builders.TestResultsBackingFormBuilder;
import org.jembi.bsis.helpers.matchers.BloodTestResultMatcher;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.model.donation.Titre;
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
  private static final UUID DONATION_ID = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837b1");
  private static final UUID FIRST_BLOOD_TEST_RESULT_ID=UUID.randomUUID();
  private static final UUID SECOND_BLOOD_TEST_RESULT_ID=UUID.randomUUID();
  private static final UUID THIRD_BLOOD_TEST_RESULT_ID=UUID.randomUUID();
  private static final UUID FOURTH_BLOOD_TEST_RESULT_ID=UUID.randomUUID();

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
  TypedQuery<BloodTestResult> typedQueryBTR;

  @Mock
  TypedQuery<BloodTest> typedQueryBT;

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
    Donation donation = aDonation().withId(DONATION_ID).withBloodAbo("A").withBloodRh("+")
        .withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    ruleResult.setTTIStatus(TTIStatus.SAFE);

    // set up mocks

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    Assert.assertFalse("Donation updated", updated);
  }

  @Test
  public void testUpdateDonationWithTestResultsUpdatedBloodAbo() throws Exception {
    // set up data
    Donation donation = aDonation().withId(DONATION_ID).withBloodRh("+").withTTIStatus(TTIStatus.NOT_DONE)
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE).withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.NOT_DONE);

    // set up mocks

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    Assert.assertTrue("Donation updated", updated);
    Assert.assertEquals("BloodAbo set", "A", donation.getBloodAbo());
  }

  public void testUpdateDonationWithTestResultsUpdatedBloodRh() throws Exception {
    // set up data
    Donation donation = aDonation().withId(DONATION_ID).withBloodAbo("A").withTTIStatus(TTIStatus.NOT_DONE)
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE).withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.NOT_DONE);

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
    Donation donation = aDonation().withId(DONATION_ID).withBloodAbo("A").withBloodRh("+")
        .withTTIStatus(TTIStatus.NOT_DONE).withBloodTypingStatus(BloodTypingStatus.NOT_DONE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE).build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.SAFE);

    // set up mocks

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    Assert.assertTrue("Donation updated", updated);
    Assert.assertEquals("TTI status set", TTIStatus.SAFE, donation.getTTIStatus());
  }

  @Test
  public void testUpdateDonationWithTestResultsUpdatedBloodTypingStatus() throws Exception {
    // set up data
    Donation donation = aDonation().withId(DONATION_ID).withBloodAbo("A").withBloodRh("+")
        .withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE).build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.PENDING_TESTS);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.SAFE);

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
    Donation donation = aDonation().withId(DONATION_ID).withBloodAbo("A").withBloodRh("+")
        .withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE).build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    ruleResult.setTTIStatus(TTIStatus.SAFE);

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
    Donation donation = aDonation().withId(DONATION_ID).withTTIStatus(TTIStatus.SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE).withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.COMPLETE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH);
    ruleResult.setTTIStatus(TTIStatus.UNSAFE);

    // set up mocks

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    Assert.assertTrue("Donation updated", updated);
    Assert.assertEquals("TTI status set", TTIStatus.UNSAFE, donation.getTTIStatus());
  }

  @Test
  public void testUpdateDonationWithTestResultsUpdatedTitreFromNull_shouldUpdate() throws Exception {
    // set up data
    Donation donation = aDonation().withId(DONATION_ID).withTitre(null).build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setTitre(Titre.LOW);

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    assertThat(updated, is(true));
    assertThat(donation.getTitre(), is(Titre.LOW));
  }

  @Test
  public void testUpdateDonationWithTestResultsUpdatedTitreFromHighToLow_shouldUpdate() throws Exception {
    // set up data
    Donation donation = aDonation().withId(DONATION_ID).withTitre(Titre.HIGH).build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setTitre(Titre.LOW);

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    assertThat(updated, is(true));
    assertThat(donation.getTitre(), is(Titre.LOW));
  }

  @Test
  public void testUpdateDonationWithTestResultsUpdatedTitreFromHighToNT_shouldUpdate() throws Exception {
    // set up data
    Donation donation = aDonation().withId(DONATION_ID).withTitre(Titre.HIGH).build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setTitre(null);

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    assertThat(updated, is(true));
    assertThat(donation.getTitre(), nullValue());
  }

  @Test
  public void testUpdateDonationWithTestResultsSameTitre_shouldNotUpdate() throws Exception {
    // set up data
    Donation donation = aDonation().withId(DONATION_ID).withTitre(Titre.HIGH).build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setTitre(Titre.HIGH);

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    assertThat(updated, is(false));
    assertThat(donation.getTitre(), is(Titre.HIGH));
  }

  @Test
  public void testSaveBloodTestingResultsTestBatchNotReleased() throws Exception {
    // set up data
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.OPEN).build();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().build();
    Donation donation = aDonation().withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)
        .withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withDonationBatch(donationBatch)
        .withTestBatch(testBatch).build();

    UUID bloodTestId = UUID.randomUUID();
    Map<UUID, String> bloodTestResults = new HashMap<>();
    bloodTestResults.put(bloodTestId, "AB");
    BloodTest bloodTest = BloodTestBuilder.aBloodTest().withId(bloodTestId).build();
    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(FIRST_BLOOD_TEST_RESULT_ID).withBloodTest(bloodTest).build());

    BloodTestingRuleResult ruleResult = aBloodTestingRuleResult().withBloodAbo("AB")
        .withBloodRh("+").withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).build();

    // set up mocks
    when(bloodTestRepository.findBloodTestById(bloodTestId)).thenReturn(bloodTest);
    when(donationRepository.findDonationById(donation.getId())).thenReturn(donation);
    when(donationRepository.findDonationByDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)).thenReturn(donation);
    when(ruleEngine.applyBloodTests(donation, bloodTestResults)).thenReturn(ruleResult);
    when(entityManager.createQuery("SELECT btr FROM BloodTestResult btr WHERE "
        + "btr.donation.id=:donationId AND btr.isDeleted = :testOutcomeDeleted "
        + "AND btr.bloodTest.isActive= :isActive AND btr.bloodTest.isDeleted= :isDeleted", BloodTestResult.class))
            .thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("donationId", 1)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("testOutcomeDeleted", false)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("isActive", true)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("isDeleted", false)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.getResultList()).thenReturn(bloodTestResultList);
    when(entityManager.createQuery("SELECT bt FROM BloodTest bt WHERE " + "bt.id=:bloodTestId", BloodTest.class))
        .thenReturn(typedQueryBT);
    when(typedQueryBT.setParameter("bloodTestId", bloodTestId)).thenReturn(typedQueryBT);
    when(typedQueryBT.getSingleResult()).thenReturn(bloodTest);

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
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().build();
    Donation donation = aDonation().withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)
        .withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withDonationBatch(donationBatch)
        .withTestBatch(testBatch).build();

    UUID bloodTestId = UUID.randomUUID();
    Map<UUID, String> bloodTestResults = new HashMap<>();
    bloodTestResults.put(bloodTestId, "AB");
    BloodTest bloodTest = BloodTestBuilder.aBloodTest().withId(bloodTestId).build();
    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(FIRST_BLOOD_TEST_RESULT_ID).withBloodTest(bloodTest).build());

    BloodTestingRuleResult ruleResult = aBloodTestingRuleResult().withBloodAbo("AB")
        .withBloodRh("+").withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).build();

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)).thenReturn(donation);
    when(ruleEngine.applyBloodTests(donation, bloodTestResults)).thenReturn(ruleResult);
    when(entityManager.createQuery("SELECT btr FROM BloodTestResult btr WHERE "
        + "btr.donation.id=:donationId AND btr.isDeleted = :testOutcomeDeleted "
        + "AND btr.bloodTest.isActive= :isActive AND btr.bloodTest.isDeleted= :isDeleted", BloodTestResult.class))
            .thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("donationId", 1)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("testOutcomeDeleted", false)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("isActive", true)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("isDeleted", false)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.getResultList()).thenReturn(bloodTestResultList);
    when(entityManager.createQuery("SELECT bt FROM BloodTest bt WHERE " + "bt.id=:bloodTestId", BloodTest.class))
        .thenReturn(typedQueryBT);
    when(typedQueryBT.setParameter("bloodTestId", bloodTestId)).thenReturn(typedQueryBT);
    when(typedQueryBT.getSingleResult()).thenReturn(bloodTest);

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
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().build();
    Donation donation = aDonation().withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)
        .withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withDonationBatch(donationBatch)
        .withTestBatch(testBatch).build();

    UUID bloodTestId = UUID.randomUUID();

    Map<UUID, String> bloodTestResults = new HashMap<>(); // tests passed to the 'saveBloodTests'
                                                          // service method
    bloodTestResults.put(bloodTestId, "AB");

    Map<UUID, String> reEnteredBloodTestResults = new HashMap<>(); // tests passed to the rules
                                                                   // engine

    BloodTest bloodTest = BloodTestBuilder.aBloodTest().withId(bloodTestId).build();
    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(FIRST_BLOOD_TEST_RESULT_ID).withBloodTest(bloodTest).build());

    BloodTestingRuleResult ruleResult = aBloodTestingRuleResult().withBloodAbo("AB")
        .withBloodRh("+").withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).build();

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)).thenReturn(donation);
    when(ruleEngine.applyBloodTests(donation, reEnteredBloodTestResults)).thenReturn(ruleResult).thenReturn(ruleResult);
    when(entityManager.createQuery("SELECT btr FROM BloodTestResult btr WHERE "
        + "btr.donation.id=:donationId AND btr.isDeleted = :testOutcomeDeleted "
        + "AND btr.bloodTest.isActive= :isActive AND btr.bloodTest.isDeleted= :isDeleted", BloodTestResult.class))
            .thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("donationId", 1)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("testOutcomeDeleted", false)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("isActive", true)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("isDeleted", false)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.getResultList()).thenReturn(bloodTestResultList);
    when(entityManager.createQuery("SELECT bt FROM BloodTest bt WHERE " + "bt.id=:bloodTestId", BloodTest.class))
        .thenReturn(typedQueryBT);
    when(typedQueryBT.setParameter("bloodTestId", bloodTestId)).thenReturn(typedQueryBT);
    when(typedQueryBT.getSingleResult()).thenReturn(bloodTest);
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
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().build();
    Donation donation = aDonation().withTTIStatus(TTIStatus.SAFE)
        .withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withDonationBatch(donationBatch)
        .withTestBatch(testBatch).build();

    UUID bloodTestId = UUID.randomUUID();
    Map<UUID, String> bloodTestResults = new HashMap<>();
    bloodTestResults.put(bloodTestId, "AB");
    BloodTest bloodTest = BloodTestBuilder.aBloodTest().withId(bloodTestId).build();
    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(FIRST_BLOOD_TEST_RESULT_ID).withBloodTest(bloodTest).build());

    BloodTestingRuleResult ruleResult = aBloodTestingRuleResult().withBloodAbo("AB")
        .withBloodRh("+").withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).build();

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)).thenReturn(donation);
    when(ruleEngine.applyBloodTests(donation, bloodTestResults)).thenReturn(ruleResult);
    when(entityManager.createQuery("SELECT btr FROM BloodTestResult btr WHERE "
        + "btr.donation.id=:donationId AND btr.isDeleted = :testOutcomeDeleted "
        + "AND btr.bloodTest.isActive= :isActive AND btr.bloodTest.isDeleted= :isDeleted", BloodTestResult.class))
            .thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("donationId", 1)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("testOutcomeDeleted", false)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("isActive", true)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("isDeleted", false)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.getResultList()).thenReturn(bloodTestResultList);
    when(entityManager.createQuery("SELECT bt FROM BloodTest bt WHERE " + "bt.id=:bloodTestId", BloodTest.class))
        .thenReturn(typedQueryBT);
    when(typedQueryBT.setParameter("bloodTestId", bloodTestId)).thenReturn(typedQueryBT);
    when(typedQueryBT.getSingleResult()).thenReturn(bloodTest);
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
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().build();
    Donation donation1 = aDonation().withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1)
        .withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withDonationBatch(donationBatch)
        .withTestBatch(testBatch).build();
    Donation donation2 = aDonation().withDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_2)
        .withTTIStatus(TTIStatus.SAFE).withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH).withDonationBatch(donationBatch)
        .withTestBatch(testBatch).build();

    UUID bloodTestId = UUID.randomUUID();
    Map<UUID, String> bloodTestResults = new HashMap<>();
    bloodTestResults.put(bloodTestId, "AB");
    BloodTest bloodTest = BloodTestBuilder.aBloodTest().withId(bloodTestId).build();
    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(FIRST_BLOOD_TEST_RESULT_ID).withBloodTest(bloodTest).build());

    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_1))
        .thenReturn(donation1);
    when(donationRepository.findDonationByDonationIdentificationNumber(IRRELEVANT_DONATION_DIN_2))
        .thenReturn(donation2);
    when(entityManager.createQuery("SELECT btr FROM BloodTestResult btr WHERE "
        + "btr.donation.id=:donationId AND btr.isDeleted = :testOutcomeDeleted "
        + "AND btr.bloodTest.isActive= :isActive AND btr.bloodTest.isDeleted= :isDeleted", BloodTestResult.class))
            .thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("donationId", 1)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("testOutcomeDeleted", false)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("isActive", true)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.setParameter("isDeleted", false)).thenReturn(typedQueryBTR);
    when(typedQueryBTR.getResultList()).thenReturn(bloodTestResultList);
    when(entityManager.createQuery("SELECT bt FROM BloodTest bt WHERE " + "bt.id=:bloodTestId", BloodTest.class))
        .thenReturn(typedQueryBT);
    when(typedQueryBT.setParameter("bloodTestId", bloodTestId)).thenReturn(typedQueryBT);

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
    Donation donation = aDonation().build();
    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(FIRST_BLOOD_TEST_RESULT_ID).withDonation(donation).build());
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(SECOND_BLOOD_TEST_RESULT_ID).withDonation(donation).build());
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(THIRD_BLOOD_TEST_RESULT_ID).withDonation(donation).build());
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(FOURTH_BLOOD_TEST_RESULT_ID).withDonation(donation).build());

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

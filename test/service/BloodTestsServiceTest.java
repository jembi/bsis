package service;


import static org.mockito.Mockito.when;
import helpers.builders.BloodTestBuilder;
import helpers.builders.BloodTestResultBuilder;
import helpers.builders.BloodTestingRuleResultBuilder;
import helpers.builders.DonationBatchBuilder;
import helpers.builders.DonationBuilder;
import helpers.builders.TestBatchBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import model.bloodtesting.BloodTest;
import model.bloodtesting.BloodTestResult;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import model.donationbatch.DonationBatch;
import model.testbatch.TestBatch;
import model.testbatch.TestBatchStatus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import repository.DonationRepository;
import repository.bloodtesting.BloodTestingRepository;
import repository.bloodtesting.BloodTestingRuleEngine;
import repository.bloodtesting.BloodTypingMatchStatus;
import repository.bloodtesting.BloodTypingStatus;
import viewmodel.BloodTestingRuleResult;

@RunWith(MockitoJUnitRunner.class)
public class BloodTestsServiceTest {

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

  @Before
  public void setup() {
    service.setBloodTestingRepository(bloodTestingRepository);
  }

  @Test
  public void testUpdateDonationWithTestResultsNotUpdated() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation()
        .withId(1l)
        .withBloodAbo("A")
        .withBloodRh("+")
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withExtraBloodTypeInformation("")
        .build();
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
    Donation donation = DonationBuilder.aDonation()
        .withId(1l)
        .withBloodRh("+")
        .withTTIStatus(TTIStatus.NOT_DONE)
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
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
    Donation donation = DonationBuilder.aDonation()
        .withId(1l)
        .withBloodAbo("A")
        .withTTIStatus(TTIStatus.NOT_DONE)
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
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
    Donation donation = DonationBuilder.aDonation()
        .withId(1l)
        .withBloodAbo("A")
        .withBloodRh("+")
        .withTTIStatus(TTIStatus.NOT_DONE)
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .build();
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
    Donation donation = DonationBuilder.aDonation()
        .withId(1l)
        .withBloodAbo("A")
        .withBloodRh("+")
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .build();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.AMBIGUOUS);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.TTI_SAFE);
    ruleResult.setExtraInformation(new HashSet<String>());

    // set up mocks

    // run test
    boolean updated = service.updateDonationWithTestResults(donation, ruleResult);

    // do asserts
    Assert.assertTrue("Donation updated", updated);
    Assert.assertEquals("BloodTypingStatus set", BloodTypingStatus.AMBIGUOUS, donation.getBloodTypingStatus());
  }

  @Test
  public void testUpdateDonationWithTestResultsUpdatedBloodTypingMatchStatus() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation()
        .withId(1l)
        .withBloodAbo("A")
        .withBloodRh("+")
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .build();
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
    Assert.assertEquals("BloodTypingMatchStatus set", BloodTypingMatchStatus.MATCH, donation.getBloodTypingMatchStatus());
  }

  @Test
  public void testUpdateDonationWithTestResultsUpdatedNotTTISafe() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation()
        .withId(1l)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
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
    Donation donation = DonationBuilder.aDonation()
        .withId(1l)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withExtraBloodTypeInformation(null)
        .build();
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
    Donation donation = DonationBuilder.aDonation()
        .withId(1l)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withExtraBloodTypeInformation("test2")
        .build();
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
    Donation donation = DonationBuilder.aDonation()
        .withId(1l)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withExtraBloodTypeInformation("test2")
        .build();
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
    Donation donation = DonationBuilder.aDonation()
        .withId(1l)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withExtraBloodTypeInformation("test3")
        .build();
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
    Assert.assertTrue("Extra information set correctly", "test3test1,test2".equals(updatedExtraInformation) || "test3test2,test1".equals(updatedExtraInformation));
  }

  @Test
  public void testValidateTestResultValuesValidResult() throws Exception {
    // set up data
    Map<Long, String> bloodTypingTestResults = new HashMap<>();
    bloodTypingTestResults.put(1l, "A"); // invalid result
    bloodTypingTestResults.put(2l, "POS"); // invalid result

    List<BloodTest> tests = new ArrayList<>();
    tests.add(BloodTestBuilder.aBloodTest().withId(1l).withValidResults("A,B,AB,O").withIsEmptyAllowed(true).build());
    tests.add(BloodTestBuilder.aBloodTest().withId(2l).withValidResults("POS,NEG").withIsEmptyAllowed(true).build());

    // set up mocks
    //when(bloodTestingRepository.findActiveBloodTests()).thenReturn(tests);
    when(entityManager.createQuery("SELECT b FROM BloodTest b WHERE b.isActive = :isActive ", BloodTest.class)).thenReturn(typedQuery);
    when(typedQuery.setParameter("isActive", true)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(tests);

    // run test
    Map<Long, String> errorMap = service.validateTestResultValues(bloodTypingTestResults);

    // do asserts
    Assert.assertNotNull("Results returned", errorMap);
    Assert.assertEquals("errors found", 0, errorMap.size());
  }

  @Test
  public void testValidateTestResultValuesInvalidResult() throws Exception {
    // set up data
    Map<Long, String> bloodTypingTestResults = new HashMap<>();
    bloodTypingTestResults.put(1l, "G"); // invalid result
    bloodTypingTestResults.put(2l, "FALSE"); // invalid result

    List<BloodTest> tests = new ArrayList<>();
    tests.add(BloodTestBuilder.aBloodTest().withId(1l).withValidResults("A,B,AB,O").withIsEmptyAllowed(true).build());
    tests.add(BloodTestBuilder.aBloodTest().withId(2l).withValidResults("POS,NEG").withIsEmptyAllowed(true).build());

    // set up mocks
    //when(bloodTestingRepository.findActiveBloodTests()).thenReturn(tests);
    when(entityManager.createQuery("SELECT b FROM BloodTest b WHERE b.isActive = :isActive ", BloodTest.class)).thenReturn(typedQuery);
    when(typedQuery.setParameter("isActive", true)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(tests);

    // run test
    Map<Long, String> errorMap = service.validateTestResultValues(bloodTypingTestResults);

    // do asserts
    Assert.assertNotNull("Results returned", errorMap);
    Assert.assertEquals("errors found", 1, errorMap.size()); // note: stops after 1st error encountered
    Assert.assertEquals("error message correct", "Invalid value specified", errorMap.get(1l));
  }

  @Test
  public void testValidateTestResultValuesNoTestResults() throws Exception {
    // set up data
    Map<Long, String> bloodTypingTestResults = new HashMap<>();

    List<BloodTest> tests = new ArrayList<>();
    tests.add(BloodTestBuilder.aBloodTest().withId(1l).withValidResults("A,B,AB,O").withIsEmptyAllowed(true).build());
    tests.add(BloodTestBuilder.aBloodTest().withId(2l).withValidResults("POS,NEG").withIsEmptyAllowed(true).build());

    // set up mocks
    //when(bloodTestingRepository.findActiveBloodTests()).thenReturn(tests);
    when(entityManager.createQuery("SELECT b FROM BloodTest b WHERE b.isActive = :isActive ", BloodTest.class)).thenReturn(typedQuery);
    when(typedQuery.setParameter("isActive", true)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(tests);

    // run test
    Map<Long, String> errorMap = service.validateTestResultValues(bloodTypingTestResults);

    // do asserts
    Assert.assertNotNull("Results returned", errorMap);
    Assert.assertEquals("errors found", 0, errorMap.size());
  }

  @Test
  public void testValidateTestResultValuesNoValueSpecified() throws Exception {
    // set up data
    Map<Long, String> bloodTypingTestResults = new HashMap<>();
    bloodTypingTestResults.put(2l, "");

    List<BloodTest> tests = new ArrayList<>();
    tests.add(BloodTestBuilder.aBloodTest().withId(1l).withValidResults("A,B,AB,O").withIsEmptyAllowed(false).build());
    tests.add(BloodTestBuilder.aBloodTest().withId(2l).withValidResults("POS,NEG").withIsEmptyAllowed(false).build());

    // set up mocks
    //when(bloodTestingRepository.findActiveBloodTests()).thenReturn(tests);
    when(entityManager.createQuery("SELECT b FROM BloodTest b WHERE b.isActive = :isActive ", BloodTest.class)).thenReturn(typedQuery);
    when(typedQuery.setParameter("isActive", true)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(tests);

    // run test
    Map<Long, String> errorMap = service.validateTestResultValues(bloodTypingTestResults);

    // do asserts
    Assert.assertNotNull("Results returned", errorMap);
    Assert.assertEquals("errors found", 1, errorMap.size());
    Assert.assertEquals("error message correct", "No value specified", errorMap.get(2l));
  }

  @Test
  public void testValidateTestResultValuesNoValueSpecifiedEmptyAllowed() throws Exception {
    // set up data
    Map<Long, String> bloodTypingTestResults = new HashMap<>();
    bloodTypingTestResults.put(2l, "");

    List<BloodTest> tests = new ArrayList<>();
    // FIXME: had to force an empty string into the valid results
    tests.add(BloodTestBuilder.aBloodTest().withId(2l).withValidResults("POS,,NEG").withIsEmptyAllowed(true).build());

    // set up mocks
    //when(bloodTestingRepository.findActiveBloodTests()).thenReturn(tests);
    when(entityManager.createQuery("SELECT b FROM BloodTest b WHERE b.isActive = :isActive ", BloodTest.class)).thenReturn(typedQuery);
    when(typedQuery.setParameter("isActive", true)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(tests);

    // run test
    Map<Long, String> errorMap = service.validateTestResultValues(bloodTypingTestResults);

    // do asserts
    Assert.assertNotNull("Results returned", errorMap);
    Assert.assertEquals("errors found", 0, errorMap.size());
  }

  @Test
  public void testValidateTestResultValuesInvalidTest() throws Exception {
    // set up data
    Map<Long, String> bloodTypingTestResults = new HashMap<>();
    bloodTypingTestResults.put(123l, "FALSE");

    List<BloodTest> tests = new ArrayList<>();
    tests.add(BloodTestBuilder.aBloodTest().withId(1l).build());

    // set up mocks
    //when(bloodTestingRepository.findActiveBloodTests()).thenReturn(tests);
    when(entityManager.createQuery("SELECT b FROM BloodTest b WHERE b.isActive = :isActive ", BloodTest.class)).thenReturn(typedQuery);
    when(typedQuery.setParameter("isActive", true)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(tests);

    // run test
    Map<Long, String> errorMap = service.validateTestResultValues(bloodTypingTestResults);

    // check asserts
    Assert.assertNotNull("Results returned", errorMap);
    Assert.assertEquals("errors found", 1, errorMap.size());
    Assert.assertEquals("error message correct", "Invalid test", errorMap.get(123l));
  }

  @Test
  public void testSaveBloodTestingResultsTestBatchNotReleased() throws Exception {
    // set up data
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.OPEN).build();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().withTestBatch(testBatch).build();
    Donation donation = DonationBuilder.aDonation()
        .withId(1l)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withDonationBatch(donationBatch)
        .build();

    Map<Long, String> bloodTestResults = new HashMap<>();
    bloodTestResults.put(1l, "AB");
    BloodTest bloodTest = BloodTestBuilder.aBloodTest().withId(17l).build();
    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(1l).withBloodTest(bloodTest).build());
    
    BloodTestingRuleResult ruleResult = BloodTestingRuleResultBuilder.aBloodTestingRuleResult()
        .withBloodAbo("AB")
        .withBloodRh("+")
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withExtraInformation(new HashSet<String>())
        .build();

    // set up mocks
    when(donationRepository.findDonationById(donation.getId())).thenReturn(donation);
    when(ruleEngine.applyBloodTests(donation, bloodTestResults)).thenReturn(ruleResult);
    when(entityManager.createQuery("SELECT bt FROM BloodTestResult bt WHERE " + "bt.donation.id=:donationId",
        BloodTestResult.class)).thenReturn(typedQuery);
    when(typedQuery.setParameter("donationId", 1)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(bloodTestResultList);
    when(entityManager.createQuery("SELECT bt FROM BloodTest bt WHERE " + "bt.id=:bloodTestId", BloodTest.class))
        .thenReturn(typedQuery);
    when(typedQuery.setParameter("bloodTestId", 17)).thenReturn(typedQuery);
    when(typedQuery.getSingleResult()).thenReturn(bloodTest);

    // run test
    BloodTestingRuleResult returnedRuleResult = service.saveBloodTests(donation.getId(), bloodTestResults, false);

    // check asserts
    Assert.assertNotNull("ruleResult returned", returnedRuleResult);
    Assert.assertEquals("ABO correct", "AB", donation.getBloodAbo());
    Mockito.verify(testBatchStatusChangeService, Mockito.never()).handleRelease(donation);
  }
  
  @Test
  public void testSaveBloodTestingResultsTestBatchReleased() throws Exception {
    // set up data
    TestBatch testBatch = TestBatchBuilder.aTestBatch().withStatus(TestBatchStatus.RELEASED).build();
    DonationBatch donationBatch = DonationBatchBuilder.aDonationBatch().withTestBatch(testBatch).build();
    Donation donation = DonationBuilder.aDonation()
        .withId(1l)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withDonationBatch(donationBatch)
        .build();

    Map<Long, String> bloodTestResults = new HashMap<>();
    bloodTestResults.put(1l, "AB");
    BloodTest bloodTest = BloodTestBuilder.aBloodTest().withId(17l).build();
    List<BloodTestResult> bloodTestResultList = new ArrayList<>();
    bloodTestResultList.add(BloodTestResultBuilder.aBloodTestResult().withId(1l).withBloodTest(bloodTest).build());
    
    BloodTestingRuleResult ruleResult = BloodTestingRuleResultBuilder.aBloodTestingRuleResult()
        .withBloodAbo("AB")
        .withBloodRh("+")
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withExtraInformation(new HashSet<String>())
        .build();

    // set up mocks
    when(donationRepository.findDonationById(donation.getId())).thenReturn(donation);
    when(ruleEngine.applyBloodTests(donation, bloodTestResults)).thenReturn(ruleResult);
    when(entityManager.createQuery("SELECT bt FROM BloodTestResult bt WHERE " + "bt.donation.id=:donationId",
        BloodTestResult.class)).thenReturn(typedQuery);
    when(typedQuery.setParameter("donationId", 1)).thenReturn(typedQuery);
    when(typedQuery.getResultList()).thenReturn(bloodTestResultList);
    when(entityManager.createQuery("SELECT bt FROM BloodTest bt WHERE " + "bt.id=:bloodTestId", BloodTest.class))
        .thenReturn(typedQuery);
    when(typedQuery.setParameter("bloodTestId", 17)).thenReturn(typedQuery);
    when(typedQuery.getSingleResult()).thenReturn(bloodTest);

    // run test
    BloodTestingRuleResult returnedRuleResult = service.saveBloodTests(donation.getId(), bloodTestResults, false);

    // check asserts
    Assert.assertNotNull("ruleResult returned", returnedRuleResult);
    Assert.assertEquals("ABO correct", "AB", donation.getBloodAbo());
    Mockito.verify(testBatchStatusChangeService).handleRelease(donation);
  }
}

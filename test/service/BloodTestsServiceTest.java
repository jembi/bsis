package service;


import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import helpers.builders.BloodTestBuilder;
import helpers.builders.BloodTestingRuleResultBuilder;
import helpers.builders.DonationBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.bloodtesting.BloodTest;
import model.bloodtesting.TSVFileHeaderName;
import model.bloodtesting.TTIStatus;
import model.donation.Donation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
  
  @Mock
  BloodTestingRepository bloodTestingRepository;
  
  @Mock
  BloodTestingRuleEngine ruleEngine;
  
  @Mock
  DonationRepository donationRepository;

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
    Set<String> newExtraInformation = new HashSet<>();
    newExtraInformation.add("test1");
    
    // set up mocks
    
    // run test
    String updatedExtraInformation = service.addNewExtraInformation(null, newExtraInformation);
    
    // do asserts
    Assert.assertEquals("Extra information set correctly", "test1", updatedExtraInformation);
  }
  
  @Test
  public void testAddNewExtraInformationOne() throws Exception {
    // set up data
    Set<String> newExtraInformation = new HashSet<>();
    
    // set up mocks
    
    // run test
    String updatedExtraInformation = service.addNewExtraInformation("test2", newExtraInformation);
    
    // do asserts
    Assert.assertEquals("Extra information set correctly", "test2", updatedExtraInformation);
  }
  
  @Test
  public void testAddNewExtraInformationTwo() throws Exception {
    // set up data
    Set<String> newExtraInformation = new HashSet<>();
    newExtraInformation.add("test1");
    
    // set up mocks
    
    // run test
    String updatedExtraInformation = service.addNewExtraInformation("test2", newExtraInformation);
    
    // do asserts
    Assert.assertEquals("Extra information set correctly", "test2test1", updatedExtraInformation);
  }
  
  @Test
  public void testAddNewExtraInformationThree() throws Exception {
    // set up data
    Set<String> newExtraInformation = new HashSet<>();
    newExtraInformation.add("test1");
    newExtraInformation.add("test2");
    
    // set up mocks
    
    // run test
    String updatedExtraInformation = service.addNewExtraInformation("test3", newExtraInformation);
    
    // do asserts
    Assert.assertEquals("Extra information set correctly", "test3test1,test2", updatedExtraInformation);
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
    when(bloodTestingRepository.findActiveBloodTests()).thenReturn(tests);
    
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
    when(bloodTestingRepository.findActiveBloodTests()).thenReturn(tests);
    
    // run test
    Map<Long, String> errorMap = service.validateTestResultValues(bloodTypingTestResults);
    
    // do asserts
    Assert.assertNotNull("Results returned", errorMap);
    Assert.assertEquals("errors found", 1, errorMap.size()); // note: stops after 1st error encountered
    Assert.assertEquals("error message correct",  "Invalid value specified", errorMap.get(1l));
  }
  
  @Test
  public void testValidateTestResultValuesNoTestResults() throws Exception {
    // set up data
    Map<Long, String> bloodTypingTestResults = new HashMap<>();
    
    List<BloodTest> tests = new ArrayList<>();
    tests.add(BloodTestBuilder.aBloodTest().withId(1l).withValidResults("A,B,AB,O").withIsEmptyAllowed(true).build());
    tests.add(BloodTestBuilder.aBloodTest().withId(2l).withValidResults("POS,NEG").withIsEmptyAllowed(true).build());
    
    // set up mocks
    when(bloodTestingRepository.findActiveBloodTests()).thenReturn(tests);
    
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
    when(bloodTestingRepository.findActiveBloodTests()).thenReturn(tests);

    // run test
    Map<Long, String> errorMap = service.validateTestResultValues(bloodTypingTestResults);
    
    // do asserts
    Assert.assertNotNull("Results returned", errorMap);
    Assert.assertEquals("errors found", 1, errorMap.size());
    Assert.assertEquals("error message correct",  "No value specified", errorMap.get(2l));
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
    when(bloodTestingRepository.findActiveBloodTests()).thenReturn(tests);

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
    when(bloodTestingRepository.findActiveBloodTests()).thenReturn(tests);
    
    // run test
    Map<Long, String> errorMap = service.validateTestResultValues(bloodTypingTestResults);
    
    // check asserts
    Assert.assertNotNull("Results returned", errorMap);
    Assert.assertEquals("errors found", 1, errorMap.size());
    Assert.assertEquals("error message correct",  "Invalid test", errorMap.get(123l));
  }

  
  @Test
  public void testSaveBloodTestingResults() throws Exception {
    // set up data
    Donation donation = DonationBuilder.aDonation()
        .withId(1l)
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .build();

    Map<Long, String> bloodTestResults = new HashMap<>();
    bloodTestResults.put(1l, "AB");
    
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
    
    // run test
    BloodTestingRuleResult returnedRuleResult = service.saveBloodTests(donation.getId(), bloodTestResults);
    
    // check asserts
    Assert.assertNotNull("ruleResult returned", returnedRuleResult);
    Assert.assertEquals("ABO correct", "AB", donation.getBloodAbo());
  }
  
  @Test
  public void testSaveTestResult() throws Exception {
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
    
    List<TSVFileHeaderName> tsv = new ArrayList<>();
    TSVFileHeaderName tsv1 = new TSVFileHeaderName();
    tsv1.setAssayNumber(1);
    Date testedOn = new Date();
    tsv1.setCompleted(testedOn);
    tsv1.setInterpretation("A");
    tsv1.setSID("DIN123");
    tsv.add(tsv1);
    
    BloodTestingRuleResult ruleResult = BloodTestingRuleResultBuilder.aBloodTestingRuleResult()
        .withBloodAbo("A")
        .withBloodRh("+")
        .withTTIStatus(TTIStatus.TTI_SAFE)
        .withBloodTypingStatus(BloodTypingStatus.COMPLETE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.MATCH)
        .withExtraInformation(new HashSet<String>())
        .build();
    
    
    // set up mocks
    when(donationRepository.findDonationByDonationIdentificationNumber("DIN123")).thenReturn(donation);
    when(ruleEngine.applyBloodTests(donation, new HashMap<Long, String>())).thenReturn(ruleResult);
    when(bloodTestingRepository.saveBloodTestResultToDatabase(1l, "A", donation, testedOn, ruleResult)).thenReturn(null);
    
    service.saveTestResults(tsv);
    
    // do asserts
    verify(bloodTestingRepository).saveBloodTestResultToDatabase(1l, "A", donation, testedOn, ruleResult);
  }
}

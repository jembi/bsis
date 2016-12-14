package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRepository;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the BloodTestingRepository
 */
public class BloodTestingRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  BloodTestingRepository bloodTestingRepository;

  @Autowired
  DonationRepository donationRepository;
    
  @Override
  protected IDataSet getDataSet() throws Exception {
      File file = new File("src/test/resources/dataset/BloodTestingRepositoryDataset.xml");
      return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Test
  public void testReEntryRequiredAfterTTIEdit() throws Exception {

    // Update test 17 to POS and check that the reEntryRequired field is updated to true only
    // for that test. All tests are NEG to start with.
    Donation donation = donationRepository.findDonationById(8l);
    Map<Long, String> stringResults = new HashMap<Long, String>();
    stringResults.put(17L, "POS");
    stringResults.put(20L, "NEG");
    stringResults.put(23L, "NEG");
    stringResults.put(26L, "NEG");
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.TTI_SAFE);
    bloodTestingRepository.saveBloodTestResultsToDatabase(stringResults, donation, new Date(), ruleResult, false);

    Map<Long, BloodTestResult> newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    for (Long key : newResults.keySet()) {
      BloodTestResult result = (BloodTestResult) newResults.get(key);
      if (result.getBloodTest().getCategory().equals(BloodTestCategory.TTI)) {
        if (result.getBloodTest().getId() == 17) {
          Assert.assertEquals("Field reEntryRequired is set to true for test 17", true, result.getReEntryRequired());
        } else {
          Assert.assertEquals("Field reEntryRequired is false", false, result.getReEntryRequired());
        }
      }
    }
  }
  
  @Test
  public void testReEntrySequences() throws Exception {
    Donation donation = donationRepository.findDonationById(8l);
    Map<Long, String> testResults = new HashMap<Long, String>();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.TTI_SAFE);
    
    // #1: re-entry should be required
    testResults.put(17L, "POS");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, false);
    Map<Long, BloodTestResult> newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertTrue("Re-entry required", newResults.get(17L).getReEntryRequired());
    
    // #2: edited initial result, re-entry still required
    testResults.put(17L, "NEG");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, false);
    newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertTrue("Re-entry still required", newResults.get(17L).getReEntryRequired());

    // #3:  re-entry of last result
    testResults.put(17L, "NEG");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, true);
    newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertFalse("Re-entry no longer required", newResults.get(17L).getReEntryRequired());

    // #4:  edited initial result, but no change in outcome
    testResults.put(17L, "NEG");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, false);
    newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertFalse("Re-entry not required", newResults.get(17L).getReEntryRequired());

    // #4:  edited initial result, but there is now a change
    testResults.put(17L, "POS");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, false);
    newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertTrue("Re-entry now required", newResults.get(17L).getReEntryRequired());

    // #5:  edited initial result, but there is no change in outcome
    testResults.put(17L, "POS");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, false);
    newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertTrue("Re-entry still required", newResults.get(17L).getReEntryRequired());

    // #6:  re-entry done with a different result
    testResults.put(17L, "NEG");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, true);
    newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertFalse("Re-entry no longer required", newResults.get(17L).getReEntryRequired());
  }

  @Test
  public void testDonationTTIStatusUpdateOnlyOnReEntry() throws Exception {
    Donation donation = donationRepository.findDonationById(8l);
    Map<Long, String> testResults = new HashMap<Long, String>();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.TTI_UNSAFE);

    testResults.put(17L, "POS");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, false);
    donation = donationRepository.findDonationById(8l);
    Assert.assertTrue("Re-entry is false, so tti status remains as NOT_DONE",
        donation.getTTIStatus().equals(TTIStatus.NOT_DONE));

    testResults.put(17L, "POS");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, true);
    donation = donationRepository.findDonationById(8l);
    Assert.assertTrue("Re-entry is true, so tti status is updated to TTI_UNSAFE",
        donation.getTTIStatus().equals(TTIStatus.TTI_UNSAFE));

  }

  @Test
  public void testBloodTestResultCreationReEntryImplemented() throws Exception {
    Donation donation = donationRepository.findDonationById(9l);
    Map<Long, String> testResults = new HashMap<Long, String>();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.NOT_DONE);

    // reEntry is implemented so, on creation, send reEntry as false:
    testResults.put(17L, "POS");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, false);
    Map<Long, BloodTestResult> newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertTrue("Re-entry is required", newResults.get(17L).getReEntryRequired());
  }

  @Test
  public void testBloodTestResultCreationReEntryNotImplemented() throws Exception {
    Donation donation = donationRepository.findDonationById(9l);
    Map<Long, String> testResults = new HashMap<Long, String>();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.NOT_DONE);

    // reEntry is not implemented so, on creation send reEntry as true:
    testResults.put(17L, "POS");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, true);
    Map<Long, BloodTestResult> newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertFalse("Re-entry is not required", newResults.get(17L).getReEntryRequired());
  }

  @Test
  public void testGetTestResultsForDonationBatchesByBloodTestType() {
    ArrayList<Long> donationBatchIds = new ArrayList<Long>();
    donationBatchIds.add(2l);
    List<BloodTestingRuleResult> results = bloodTestingRepository
        .getAllTestsStatusForDonationBatchesByBloodTestType(donationBatchIds,
        BloodTestType.BASIC_TTI);

    // donation id = 2 is the only donation in batch id = 2
    BloodTestingRuleResult result = results.get(0);
    Assert.assertTrue("Number of tests of type BASIC_TTI for donation batch 2 is 4",
        result.getRecentTestResults().size() == 4);
  }
  
  @Test
  public void testGetRecentTestResultsForDonationWithActiveAndUnDeletedBloodTest_shouldReturnABloodTestResult() {
    Donation donation = donationRepository.findDonationById(9l);
    //Test
    Map<Long, BloodTestResult> returnedResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    System.out.println(returnedResults);
    BloodTestResult result = returnedResults.get(28l);
    assertThat(returnedResults.size(), is(1));
    assertThat(result.getBloodTest().getIsActive(), is(true));
    assertThat(result.getBloodTest().getIsDeleted(), is(false));
  }
  
  @Test
  public void testGetRecentTestResultsForDonationWithActiveAndDeletedBloodTest_shouldNotReturnABloodTestResult() {
    Donation donation = donationRepository.findDonationById(10l);
    //Test
    Map<Long, BloodTestResult> returnedResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    assertThat(returnedResults.size(), is(0));
  }
  
  @Test
  public void testGetRecentTestResultsForDonationWithInactiveAndUnDeletedBloodTest_shouldNotReturnABloodTestResult() {
    // Data setUp
    Donation donation = donationRepository.findDonationById(11l);
    //Test
    Map<Long, BloodTestResult> returnedResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    assertThat(returnedResults.size(), is(0));
  }
}

package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;
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
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a8");
    Donation donation = donationRepository.findDonationById(donationId);
    Map<UUID, String> stringResults = new HashMap<>();
    stringResults.put(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8617"), "POS");
    stringResults.put(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8620"), "NEG");
    stringResults.put(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8623"), "NEG");
    stringResults.put(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8626"), "NEG");
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.SAFE);
    bloodTestingRepository.saveBloodTestResultsToDatabase(stringResults, donation, new Date(), ruleResult, false);

    Map<UUID, BloodTestResult> newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    for (UUID key : newResults.keySet()) {
      BloodTestResult result = (BloodTestResult) newResults.get(key);
      if (result.getBloodTest().getCategory().equals(BloodTestCategory.TTI)) {
        if (result.getBloodTest().getId().equals(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8617"))) {
          Assert.assertEquals("Field reEntryRequired is set to true for test 17", true, result.getReEntryRequired());
        } else {
          Assert.assertEquals("Field reEntryRequired is false", false, result.getReEntryRequired());
        }
      }
    }
  }
  
  @Test
  public void testReEntrySequences() throws Exception {
    UUID testId = UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8617");
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a8");
    Donation donation = donationRepository.findDonationById(donationId);
    Map<UUID, String> testResults = new HashMap<>();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.SAFE);
    
    // #1: re-entry should be required
    testResults.put(testId, "POS");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, false);
    Map<UUID, BloodTestResult> newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertTrue("Re-entry required", newResults.get(testId).getReEntryRequired());
    
    // #2: edited initial result, re-entry still required
    testResults.put(testId, "NEG");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, false);
    newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertTrue("Re-entry still required", newResults.get(testId).getReEntryRequired());

    // #3:  re-entry of last result
    testResults.put(testId, "NEG");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, true);
    newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertFalse("Re-entry no longer required", newResults.get(testId).getReEntryRequired());

    // #4:  edited initial result, but no change in outcome
    testResults.put(testId, "NEG");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, false);
    newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertFalse("Re-entry not required", newResults.get(testId).getReEntryRequired());

    // #4:  edited initial result, but there is now a change
    testResults.put(testId, "POS");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, false);
    newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertTrue("Re-entry now required", newResults.get(testId).getReEntryRequired());

    // #5:  edited initial result, but there is no change in outcome
    testResults.put(testId, "POS");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, false);
    newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertTrue("Re-entry still required", newResults.get(testId).getReEntryRequired());

    // #6:  re-entry done with a different result
    testResults.put(testId, "NEG");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, true);
    newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertFalse("Re-entry no longer required", newResults.get(testId).getReEntryRequired());
  }

  @Test
  public void testDonationTTIStatusUpdateOnlyOnReEntry() throws Exception {
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a8");
    Donation donation = donationRepository.findDonationById(donationId);
    Map<UUID, String> testResults = new HashMap<>();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.UNSAFE);

    testResults.put(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8617"), "POS");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, false);
    donation = donationRepository.findDonationById(donationId);
    Assert.assertTrue("Re-entry is false, so tti status remains as NOT_DONE",
        donation.getTTIStatus().equals(TTIStatus.NOT_DONE));

    testResults.put(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8617"), "POS");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, true);
    donation = donationRepository.findDonationById(donationId);
    Assert.assertTrue("Re-entry is true, so tti status is updated to TTI_UNSAFE",
        donation.getTTIStatus().equals(TTIStatus.UNSAFE));

  }

  @Test
  public void testBloodTestResultCreationReEntryImplemented() throws Exception {
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a9");
    Donation donation = donationRepository.findDonationById(donationId);
    Map<UUID, String> testResults = new HashMap<>();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.NOT_DONE);

    // reEntry is implemented so, on creation, send reEntry as false:
    testResults.put(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8617"), "POS");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, false);
    Map<UUID, BloodTestResult> newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertTrue("Re-entry is required", newResults.get(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8617")).getReEntryRequired());
  }

  @Test
  public void testBloodTestResultCreationReEntryNotImplemented() throws Exception {
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a9");
    Donation donation = donationRepository.findDonationById(donationId);
    Map<UUID, String> testResults = new HashMap<>();
    BloodTestingRuleResult ruleResult = new BloodTestingRuleResult();
    ruleResult.setBloodAbo("A");
    ruleResult.setBloodRh("+");
    ruleResult.setBloodTypingStatus(BloodTypingStatus.NOT_DONE);
    ruleResult.setBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE);
    ruleResult.setTTIStatus(TTIStatus.NOT_DONE);

    // reEntry is not implemented so, on creation send reEntry as true:
    testResults.put(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8617"), "POS");
    bloodTestingRepository.saveBloodTestResultsToDatabase(testResults, donation, new Date(), ruleResult, true);
    Map<UUID, BloodTestResult> newResults = bloodTestingRepository.getRecentTestResultsForDonation(donation.getId());
    Assert.assertFalse("Re-entry is not required", newResults.get(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8617")).getReEntryRequired());
  }
  
  @Test
  public void testGetRecentTestResultsForDonationWithActiveAndUnDeletedBloodTest_shouldReturnABloodTestResult() {
    
    //Test
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a9");
    Map<UUID, BloodTestResult> returnedResults = bloodTestingRepository.getRecentTestResultsForDonation(donationId);
    BloodTestResult result = returnedResults.get(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8628"));
    assertThat(returnedResults.size(), is(1));
    assertThat(result.getBloodTest().getIsActive(), is(true));
    assertThat(result.getBloodTest().getIsDeleted(), is(false));
  }
  
  @Test
  public void testGetRecentTestResultsForDonationWithActiveAndDeletedBloodTest_shouldNotReturnABloodTestResult() {

    //Test
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837b0");
    Map<UUID, BloodTestResult> returnedResults = bloodTestingRepository.getRecentTestResultsForDonation(donationId);
    assertThat(returnedResults.size(), is(0));
  }
  
  @Test
  public void testGetRecentTestResultsForDonationWithInactiveAndUnDeletedBloodTest_shouldNotReturnABloodTestResult() {
  
    //Test
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837b1");
    Map<UUID, BloodTestResult> returnedResults = bloodTestingRepository.getRecentTestResultsForDonation(donationId);
    assertThat(returnedResults.size(), is(0));
  }
}

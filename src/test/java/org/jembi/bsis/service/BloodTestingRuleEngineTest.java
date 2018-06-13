package org.jembi.bsis.service;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.repository.DonationRepository;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.jembi.bsis.viewmodel.BloodTestResultFullViewModel;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.AfterTransaction;

/**
 * Test using DBUnit to test the BloodTestingRuleEngine. See the XML data set file called
 * BloodTestingRuleRepositoryDataset.xml which contains all the data used in the tests. Below is
 * some commented out code that can be used to generate XML files from the database.
 */
public class BloodTestingRuleEngineTest extends ContextDependentTestSuite {

  @Autowired
  BloodTestingRuleEngine bloodTestingRuleEngine;

  @Autowired
  DonationRepository donationRepository;

  @Autowired
  private DataSource dataSource;

  private IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/BloodTestingRuleRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  private IDatabaseConnection getConnection() throws SQLException {
    IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
    DatabaseConfig config = connection.getConfig();
    config.setProperty(DatabaseConfig.FEATURE_ALLOW_EMPTY_FIELDS, true);
    config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
    return connection;
  }

  @Before
  public void init() throws Exception {
    IDatabaseConnection connection = getConnection();
    try {
      IDataSet dataSet = getDataSet();
      DatabaseOperation.CLEAN_INSERT.execute(connection, dataSet);
    } finally {
      connection.close();
    }
  }

  @AfterTransaction
  public void after() throws Exception {
    IDatabaseConnection connection = getConnection();
    try {
      IDataSet dataSet = getDataSet();
      DatabaseOperation.DELETE_ALL.execute(connection, dataSet);
    }
    finally {
      connection.close();
    }
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation1_RepeatDonorCompleteTTISafe() throws Exception {
    // Donation 1 (donor 1) is for a repeat donor with matching blood tests and TTI safe tests
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a1");
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
    Assert.assertEquals("bloodTypingMatchStatus is MATCH", BloodTypingMatchStatus.MATCH, result.getBloodTypingMatchStatus());
    Assert.assertEquals("bloodTypingStatus is set", BloodTypingStatus.COMPLETE, result.getBloodTypingStatus());
    Assert.assertEquals("ttiStatus is SAFE", TTIStatus.SAFE, result.getTTIStatus());
    Assert.assertEquals("bloodAb is O", "O", result.getBloodAbo());
    Assert.assertEquals("bloodRh is +", "+", result.getBloodRh());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingRepeatAndConfirmatoryTtiTestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Map<UUID, String> tests = result.getAvailableTestResults();
    Iterator<String> testIts = tests.values().iterator();
    Assert.assertEquals("Available test result value", "O", testIts.next());
    Assert.assertEquals("Available test result value", "POS", testIts.next());
    Assert.assertEquals("Available test result value", "LOW", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation2_RepeatDonorCompleteTTISafe() throws Exception {
    // Donation 2 (donor 2) is for a repeat donor with matching blood tests and all TTI tests are safe 
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a2");
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
    Assert.assertEquals("bloodTypingMatchStatus is MATCH", BloodTypingMatchStatus.MATCH, result.getBloodTypingMatchStatus());
    Assert.assertEquals("bloodTypingStatus is set", BloodTypingStatus.COMPLETE, result.getBloodTypingStatus());
    Assert.assertEquals("ttiStatus is SAFE", TTIStatus.SAFE, result.getTTIStatus());
    Assert.assertEquals("bloodAb is A", "A", result.getBloodAbo());
    Assert.assertEquals("bloodRh is -", "-", result.getBloodRh());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingRepeatAndConfirmatoryTtiTestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("Available Test results", 7, result.getAvailableTestResults().size());
    Map<UUID, String> tests = result.getAvailableTestResults();
    Iterator<String> testIts = tests.values().iterator();
    Assert.assertEquals("Available test result value", "A", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "LOW", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation3_RepeatDonorWithNoTestResults() throws Exception {
    // Donation 3 (donor 3) is for a repeat donor with no stored test results
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a3");
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
    Assert.assertEquals("bloodTypingMatchStatus is NOT_DONE", BloodTypingMatchStatus.NOT_DONE, result.getBloodTypingMatchStatus());
    Assert.assertEquals("bloodTypingStatus is set", BloodTypingStatus.NOT_DONE, result.getBloodTypingStatus());
    Assert.assertEquals("ttiStatus is NOT_DONE", TTIStatus.NOT_DONE, result.getTTIStatus());
    Assert.assertEquals("bloodAb is empty", 0, result.getBloodAbo().length());
    Assert.assertEquals("bloodRh is empty", 0, result.getBloodRh().length());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingRepeatAndConfirmatoryTtiTestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("No availale test results", 0, result.getAvailableTestResults().size());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation3_RepeatDonorWithAdditionalTTIResults() throws Exception {
    // Donation 3 (donor 3) is for a repeat donor with no stored test results
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a3");
    Donation donation = donationRepository.findDonationById(donationId);
    Map<UUID, String> ttiTests = new HashMap<UUID, String>();
    ttiTests.put(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8617"), "NEG");
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, ttiTests);
    Assert.assertEquals("bloodTypingMatchStatus is NOT_DONE", BloodTypingMatchStatus.NOT_DONE, result.getBloodTypingMatchStatus());
    Assert.assertEquals("bloodTypingStatus is set", BloodTypingStatus.NOT_DONE, result.getBloodTypingStatus());
    Assert.assertEquals("ttiStatus is NOT_DONE", TTIStatus.NOT_DONE, result.getTTIStatus());
    Assert.assertEquals("bloodAb is empty", 0, result.getBloodAbo().length());
    Assert.assertEquals("bloodRh is empty", 0, result.getBloodRh().length());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingRepeatAndConfirmatoryTtiTestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("1 test result(s)", 1, result.getAvailableTestResults().size());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation3AndBloodTestResults_RepeatDonorWithAdditionalABOTestResults() throws Exception {
    // Donation 3 (donor 3) is for a repeat donor and doesn't have any test results recorded
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a3");
    Donation donation = donationRepository.findDonationById(donationId);
    Map<UUID, String> ttiTests = new HashMap<UUID, String>();
    ttiTests.put(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8601"), "A");
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, ttiTests);
    Assert.assertEquals("bloodTypingMatchStatus is NOT_DONE", BloodTypingMatchStatus.NOT_DONE, result.getBloodTypingMatchStatus());
    Assert.assertEquals("bloodTypingStatus is set", BloodTypingStatus.NOT_DONE, result.getBloodTypingStatus());
    Assert.assertEquals("ttiStatus is NOT_DONE", TTIStatus.NOT_DONE, result.getTTIStatus());
    Assert.assertEquals("bloodAb has a result", 1, result.getBloodAbo().length());
    Assert.assertEquals("bloodRh is empty", 0, result.getBloodRh().length());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingRepeatAndConfirmatoryTtiTestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("1 available test result(s)", 1, result.getAvailableTestResults().size());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation4_RepeatDonorAmbiguousBloodTyping() throws Exception {
    // Donation 4 (donor d) is for a repeat donor with an mismatching serology outcome and no TTI tests done
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a4");
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
    Assert.assertEquals("bloodTypingMatchStatus is AMBIGUOUS", BloodTypingMatchStatus.AMBIGUOUS, result.getBloodTypingMatchStatus());
    Assert.assertEquals("bloodTypingStatus is set", BloodTypingStatus.COMPLETE, result.getBloodTypingStatus());
    Assert.assertEquals("ttiStatus is NOT_DONE", TTIStatus.NOT_DONE, result.getTTIStatus());
    Assert.assertEquals("bloodAb is B", "B", result.getBloodAbo());
    Assert.assertEquals("bloodRh is +", "+", result.getBloodRh());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingRepeatAndConfirmatoryTtiTestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("Available Test results", 4, result.getAvailableTestResults().size());
    Map<UUID, String> tests = result.getAvailableTestResults();
    Iterator<String> testIts = tests.values().iterator();
    Assert.assertEquals("Available test result value", "B", testIts.next());
    Assert.assertEquals("Available test result value", "POS", testIts.next());
    Assert.assertEquals("Available test result value", "LOW", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation5_RepeatDonorTTIUnsafe() throws Exception {
    // Donation 5 (donor 2) is for a repeat donor with a donation matching ABO/Rh and one POS TTI test
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a5");
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
    Assert.assertEquals("bloodTypingMatchStatus is MATCH", BloodTypingMatchStatus.MATCH, result.getBloodTypingMatchStatus());
    Assert.assertEquals("bloodTypingStatus is set", BloodTypingStatus.COMPLETE, result.getBloodTypingStatus());
    Assert.assertEquals("ttiStatus is UNSAFE", TTIStatus.UNSAFE, result.getTTIStatus());
    Assert.assertEquals("bloodAb is A", "A", result.getBloodAbo());
    Assert.assertEquals("bloodRh is -", "-", result.getBloodRh());
    Assert.assertEquals("2 pending TTI tests", 2, result.getPendingRepeatAndConfirmatoryTtiTestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("Available Test results", 7, result.getAvailableTestResults().size());
    Map<UUID, String> tests = result.getAvailableTestResults();
    Iterator<String> testIts = tests.values().iterator();
    Assert.assertEquals("Available test result value", "A", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "LOW", testIts.next());
    Assert.assertEquals("Available test result value", "POS", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation6_RepeatDonorUninterpretableABORHAndNoTTITests() throws Exception {
    // donation 6 (donor 2) is for a repeat donor. the ABO test result is invalid and there are no TTI tests
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a6");
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
    Assert.assertEquals("bloodTypingMatchStatus is MATCH", BloodTypingMatchStatus.NOT_DONE, result.getBloodTypingMatchStatus());
    Assert.assertEquals("bloodTypingStatus is set", BloodTypingStatus.NOT_DONE, result.getBloodTypingStatus());
    Assert.assertEquals("ttiStatus is NOT_DONE", TTIStatus.NOT_DONE, result.getTTIStatus());
    Assert.assertEquals("bloodAb is unknown", "", result.getBloodAbo());
    Assert.assertEquals("bloodRh is unknown", "", result.getBloodRh());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingRepeatAndConfirmatoryTtiTestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("Available Test results", 3, result.getAvailableTestResults().size());
    Map<UUID, String> tests = result.getAvailableTestResults();
    Iterator<String> testIts = tests.values().iterator();
    Assert.assertEquals("Available test result value", "Z", testIts.next());
    Assert.assertEquals("Available test result value", "-", testIts.next());
    Assert.assertEquals("Available test result value", "?", testIts.next());
  }

  @Test
  public void testBloodTestingRuleEngineTTIReEntryRequiredList() throws Exception {
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837b4");
    Donation donation = donationRepository.findDonationById(donationId);
    Map<UUID, String> testResults = new HashMap<UUID, String>();
    testResults.put(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8617"), "POS");
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, testResults);

    ArrayList<UUID> reEntryRequiredTTITestIds = new ArrayList<>();
    Map<UUID, BloodTestResultFullViewModel> resultViewModelMap = result.getRecentTestResults();
    for (UUID key : resultViewModelMap.keySet()) {
      BloodTestResultFullViewModel resultFullViewModel = resultViewModelMap.get(key);
      if (resultFullViewModel.getReEntryRequired().equals(true)
          && resultFullViewModel.getBloodTest().getBloodTestType()
              .equals(BloodTestType.BASIC_TTI)) {
        reEntryRequiredTTITestIds.add(resultFullViewModel.getBloodTest().getId());
      }
    }
    Assert.assertEquals("Re-entry required TTI tests", 1, reEntryRequiredTTITestIds.size());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation8_1stTimeDonorInitial() throws Exception {
    // donation 8 is for a 1st time donor who has only had initial outcomes entered for ABO/Rh
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a8");
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
    Assert.assertEquals("bloodTypingMatchStatus is set", BloodTypingMatchStatus.NO_MATCH, result.getBloodTypingMatchStatus());
    Assert.assertEquals("bloodTypingStatus is set", BloodTypingStatus.NOT_DONE, result.getBloodTypingStatus());
    Assert.assertEquals("bloodAb is set", "O", result.getBloodAbo());
    Assert.assertEquals("bloodRh is set", "-", result.getBloodRh());
    Assert.assertEquals("Pending blood typing tests", 2, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("Availale test results", 2, result.getAvailableTestResults().size());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation9_1stTimeDonorInitialAndRepeat() throws Exception {
    // donation 9 is for a 1st time donor who has had initial and repeat outcomes entered for ABO/Rh
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837a9");
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
    Assert.assertEquals("bloodTypingMatchStatus is set", BloodTypingMatchStatus.MATCH, result.getBloodTypingMatchStatus());
    Assert.assertEquals("bloodTypingStatus is set", BloodTypingStatus.COMPLETE, result.getBloodTypingStatus());
    Assert.assertEquals("bloodAb is set", "O", result.getBloodAbo());
    Assert.assertEquals("bloodRh is set", "-", result.getBloodRh());
    Assert.assertEquals("Pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("Availale test results", 5, result.getAvailableTestResults().size());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation10_1stTimeDonorInitialAndNoRepeat() throws Exception {
    // donation 10 is for a 1st time donor who has had initial, but no repeat outcomes entered for ABO/Rh
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837b0");
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
    Assert.assertEquals("bloodTypingMatchStatus is set", BloodTypingMatchStatus.NO_MATCH, result.getBloodTypingMatchStatus());
    Assert.assertEquals("bloodTypingStatus is set", BloodTypingStatus.PENDING_TESTS, result.getBloodTypingStatus());
    Assert.assertEquals("bloodAb is set", "O", result.getBloodAbo());
    Assert.assertEquals("bloodRh is set", "-", result.getBloodRh());
    Assert.assertEquals("Pending blood typing tests", 2, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("Availale test results", 3, result.getAvailableTestResults().size());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation11_1stTimeDonorInitialAndMismatchedRepeat() throws Exception {
    // donation 11 is for a 1st time donor who has had initial and repeat outcomes entered for ABO/Rh, but the repeat outcome does not match
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837b1");
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
    Assert.assertEquals("bloodTypingMatchStatus is set", BloodTypingMatchStatus.AMBIGUOUS, result.getBloodTypingMatchStatus());
    Assert.assertEquals("bloodTypingStatus is set", BloodTypingStatus.COMPLETE, result.getBloodTypingStatus());
    Assert.assertEquals("bloodAb is set", "O", result.getBloodAbo());
    Assert.assertEquals("bloodRh is set", "-", result.getBloodRh());
    Assert.assertEquals("Pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("Availale test results", 5, result.getAvailableTestResults().size());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation12_TTIUnsafeAndFirstEntry() throws Exception {
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837b2");
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
    Assert.assertEquals("TTIStatus is NOT_DONE", TTIStatus.NOT_DONE, result.getTTIStatus());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingRepeatAndConfirmatoryTtiTestsIds().size());
  }
  
  @Test
  public void testBloodTestingRuleEngineWithDonation12_TTIUnsafeAndFirstEntryAndPendingReEntry() throws Exception {
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837b2");
    Donation donation = donationRepository.findDonationById(donationId);
    Map<UUID, String> newTtiTestResults = new HashMap<>();
    newTtiTestResults.put(UUID.fromString("04586549-1d7e-4b12-ba81-1987d11f8617"), "POS");
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, newTtiTestResults);
    Assert.assertEquals("TTIStatus is UNSAFE", TTIStatus.UNSAFE, result.getTTIStatus());
    Assert.assertEquals("No pending TTI tests", 2, result.getPendingRepeatAndConfirmatoryTtiTestsIds().size());
  }
  
  @Test
  public void testBloodTestingRuleEngineWithDonation13_TTIUnsafeAndReEntry() throws Exception {
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837b3");
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
    Assert.assertEquals("TTIStatus is UNSAFE", TTIStatus.UNSAFE, result.getTTIStatus());
    Assert.assertEquals("Pending TTI tests", 2, result.getPendingRepeatAndConfirmatoryTtiTestsIds().size());
  }
  
  @Test
  public void testBloodTestingRuleEngineWithDonation15_BloodTypingMatchStatusNoTypeDetermined() throws Exception {
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837b5");
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
    Assert.assertEquals("BloodTypingMatchStatus is NO_TYPE_DETERMINED", BloodTypingMatchStatus.NO_TYPE_DETERMINED, result.getBloodTypingMatchStatus());
    Assert.assertTrue("Blood ABO not set", StringUtils.isBlank(result.getBloodAbo()));
    Assert.assertTrue("Blood Rh not set", StringUtils.isBlank(result.getBloodRh()));
  }
  
  @Test
  public void testBloodTestingRuleEngineWithDonation16_BloodTypingMatchStatusResolved() throws Exception {
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837b6");
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
    Assert.assertEquals("BloodTypingMatchStatus is RESOLVED", BloodTypingMatchStatus.RESOLVED, result.getBloodTypingMatchStatus());
    Assert.assertEquals("Blood ABO not set", "A", result.getBloodAbo());
    Assert.assertEquals("Blood Rh not set", "+", result.getBloodRh());
  }
  
  @Test
  public void testBloodTestingRuleEngineWithDonation17_BloodTypingMatchStatusResolvedAndTTIUnsafe() throws Exception {
    UUID donationId = UUID.fromString("b98ebc98-87ed-48b9-80db-7c378a1837b7");
    Donation donation = donationRepository.findDonationById(donationId);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<UUID, String>());
    Assert.assertEquals("BloodTypingMatchStatus is RESOLVED", BloodTypingMatchStatus.RESOLVED, result.getBloodTypingMatchStatus());
    Assert.assertEquals("Blood ABO not set", "O", result.getBloodAbo());
    Assert.assertEquals("Blood Rh not set", "+", result.getBloodRh());
    Assert.assertEquals("TTIStatus is UNSAFE", TTIStatus.UNSAFE, result.getTTIStatus());
  }
}

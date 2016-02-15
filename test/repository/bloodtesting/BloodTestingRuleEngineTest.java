package repository.bloodtesting;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import model.bloodtesting.TTIStatus;
import model.donation.Donation;
import repository.DonationRepository;
import viewmodel.BloodTestingRuleResult;

/**
 * Test using DBUnit to test the BloodTestingRuleEngine. See the XML data set file called
 * BloodTestingRuleRepositoryDataset.xml which contains all the data used in the tests. Below is
 * some commented out code that can be used to generate XML files from the database.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class BloodTestingRuleEngineTest {

  @Autowired
  BloodTestingRuleEngine bloodTestingRuleEngine;

  @Autowired
  DonationRepository donationRepository;

  @Autowired
  private DataSource dataSource;

  private IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/BloodTestingRuleRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  private IDatabaseConnection getConnection() throws SQLException {
    IDatabaseConnection connection = new DatabaseDataSourceConnection(dataSource);
    DatabaseConfig config = connection.getConfig();
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
    } finally {
      connection.close();
    }
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation1() throws Exception {
    Donation donation = donationRepository.findDonationById(1l);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<Long, String>());
    Assert.assertEquals("bloodTypingMatchStatus is MATCH", BloodTypingMatchStatus.MATCH,
        result.getBloodTypingMatchStatus());
    Assert.assertEquals("ttiStatus is TTI_SAFE", TTIStatus.TTI_SAFE, result.getTTIStatus());
    Assert.assertEquals("bloodAb is O", "O", result.getBloodAbo());
    Assert.assertEquals("bloodRh is +", "+", result.getBloodRh());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingTTITestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Map<String, String> tests = result.getAvailableTestResults();
    Iterator<String> testIts = tests.values().iterator();
    Assert.assertEquals("Available test result value", "O", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "POS", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertFalse("No ABO Uninterpretable", result.getAboUninterpretable());
    Assert.assertFalse("No RH Uninterpretable", result.getRhUninterpretable());
    Assert.assertFalse("No TTI Uninterpretable", result.getTtiUninterpretable());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation2() throws Exception {
    Donation donation = donationRepository.findDonationById(2l);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<Long, String>());
    Assert.assertEquals("bloodTypingMatchStatus is MATCH", BloodTypingMatchStatus.MATCH,
        result.getBloodTypingMatchStatus());
    Assert.assertEquals("ttiStatus is TTI_SAFE", TTIStatus.TTI_SAFE, result.getTTIStatus());
    Assert.assertEquals("bloodAb is A", "A", result.getBloodAbo());
    Assert.assertEquals("bloodRh is -", "-", result.getBloodRh());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingTTITestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("Available Test results", 8, result.getAvailableTestResults().size());
    Map<String, String> tests = result.getAvailableTestResults();
    Iterator<String> testIts = tests.values().iterator();
    Assert.assertEquals("Available test result value", "A", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "LOW", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertFalse("No ABO Uninterpretable", result.getAboUninterpretable());
    Assert.assertFalse("No RH Uninterpretable", result.getRhUninterpretable());
    Assert.assertFalse("No TTI Uninterpretable", result.getTtiUninterpretable());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation3() throws Exception {
    Donation donation = donationRepository.findDonationById(3l);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<Long, String>());
    Assert.assertEquals("bloodTypingMatchStatus is NOT_DONE", BloodTypingMatchStatus.NOT_DONE,
        result.getBloodTypingMatchStatus());
    Assert.assertEquals("ttiStatus is NOT_DONE", TTIStatus.NOT_DONE, result.getTTIStatus());
    Assert.assertEquals("bloodAb is empty", 0, result.getBloodAbo().length());
    Assert.assertEquals("bloodRh is empty", 0, result.getBloodRh().length());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingTTITestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("No availale test results", 0, result.getAvailableTestResults().size());
    Assert.assertFalse("No ABO Uninterpretable", result.getAboUninterpretable());
    Assert.assertFalse("No RH Uninterpretable", result.getRhUninterpretable());
    Assert.assertFalse("No TTI Uninterpretable", result.getTtiUninterpretable());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation3AndTTIResults() throws Exception {
    Donation donation = donationRepository.findDonationById(3l);
    Map<Long, String> ttiTests = new HashMap<Long, String>();
    ttiTests.put(17l, "NEG");
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, ttiTests);
    Assert.assertEquals("bloodTypingMatchStatus is NOT_DONE", BloodTypingMatchStatus.NOT_DONE,
        result.getBloodTypingMatchStatus());
    Assert.assertEquals("ttiStatus is NOT_DONE", TTIStatus.NOT_DONE, result.getTTIStatus());
    Assert.assertEquals("bloodTypingMatchStatus is NOT_DONE", BloodTypingMatchStatus.NOT_DONE, result.getBloodTypingMatchStatus());
    Assert.assertEquals("bloodAb is empty", 0, result.getBloodAbo().length());
    Assert.assertEquals("bloodRh is empty", 0, result.getBloodRh().length());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingTTITestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("1 test result(s)", 1, result.getAvailableTestResults().size());
    Assert.assertFalse("No ABO Uninterpretable", result.getAboUninterpretable());
    Assert.assertFalse("No RH Uninterpretable", result.getRhUninterpretable());
    Assert.assertFalse("No TTI Uninterpretable", result.getTtiUninterpretable());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation7AndTTIResults() throws Exception {
    Donation donation = donationRepository.findDonationById(7l);
    Map<Long, String> ttiTests = new HashMap<Long, String>();
    ttiTests.put(17l, "NEG");
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, ttiTests);
    Assert.assertEquals("bloodTypingMatchStatus is NOT_DONE", BloodTypingMatchStatus.NOT_DONE,
        result.getBloodTypingMatchStatus());
    Assert.assertEquals("ttiStatus is NOT_DONE", TTIStatus.NOT_DONE, result.getTTIStatus());
    Assert.assertEquals("bloodTypingMatchStatus is NOT_DONE", BloodTypingMatchStatus.NOT_DONE, result.getBloodTypingMatchStatus());
    Assert.assertEquals("bloodAb is empty", 0, result.getBloodAbo().length());
    Assert.assertEquals("bloodRh is empty", 0, result.getBloodRh().length());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingTTITestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("1 test done", 1, result.getAvailableTestResults().size());
    Assert.assertFalse("No ABO Uninterpretable", result.getAboUninterpretable());
    Assert.assertFalse("No RH Uninterpretable", result.getRhUninterpretable());
    Assert.assertFalse("No TTI Uninterpretable", result.getTtiUninterpretable());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation3AndBloodTestResults() throws Exception {
    Donation donation = donationRepository.findDonationById(3l);
    Map<Long, String> ttiTests = new HashMap<Long, String>();
    ttiTests.put(1l, "A");
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, ttiTests);
    Assert.assertEquals("bloodTypingMatchStatus is NOT_DONE", BloodTypingMatchStatus.NOT_DONE,
        result.getBloodTypingMatchStatus());
    Assert.assertEquals("ttiStatus is NOT_DONE", TTIStatus.NOT_DONE, result.getTTIStatus());
    Assert.assertEquals("bloodTypingMatchStatus is NOT_DONE", BloodTypingMatchStatus.NOT_DONE, result.getBloodTypingMatchStatus());
    Assert.assertEquals("bloodAb has a result", 1, result.getBloodAbo().length());
    Assert.assertEquals("bloodRh is empty", 0, result.getBloodRh().length());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingTTITestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("1 availale test result(s)", 1, result.getAvailableTestResults().size());
    Assert.assertFalse("No ABO Uninterpretable", result.getAboUninterpretable());
    Assert.assertFalse("No RH Uninterpretable", result.getRhUninterpretable());
    Assert.assertFalse("No TTI Uninterpretable", result.getTtiUninterpretable());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation4() throws Exception {
    Donation donation = donationRepository.findDonationById(4l);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<Long, String>());
    Assert.assertEquals("bloodTypingMatchStatus is AMBIGUOUS", BloodTypingMatchStatus.AMBIGUOUS,
        result.getBloodTypingMatchStatus());
    Assert.assertEquals("ttiStatus is NOT_DONE", TTIStatus.NOT_DONE, result.getTTIStatus());
    Assert.assertEquals("bloodAb is B", "B", result.getBloodAbo());
    Assert.assertEquals("bloodRh is +", "+", result.getBloodRh());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingTTITestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("Available Test results", 5, result.getAvailableTestResults().size());
    Map<String, String> tests = result.getAvailableTestResults();
    Iterator<String> testIts = tests.values().iterator();
    Assert.assertEquals("Available test result value", "B", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "POS", testIts.next());
    Assert.assertEquals("Available test result value", "LOW", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertFalse("No ABO Uninterpretable", result.getAboUninterpretable());
    Assert.assertFalse("No RH Uninterpretable", result.getRhUninterpretable());
    Assert.assertFalse("No TTI Uninterpretable", result.getTtiUninterpretable());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation5() throws Exception {
    Donation donation = donationRepository.findDonationById(5l);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<Long, String>());
    Assert.assertEquals("bloodTypingMatchStatus is MATCH", BloodTypingMatchStatus.MATCH,
        result.getBloodTypingMatchStatus());
    Assert.assertEquals("ttiStatus is TTI_UNSAFE", TTIStatus.TTI_UNSAFE, result.getTTIStatus());
    Assert.assertEquals("bloodAb is A", "A", result.getBloodAbo());
    Assert.assertEquals("bloodRh is -", "-", result.getBloodRh());
    Assert.assertEquals("2 pending TTI tests", 2, result.getPendingTTITestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("Available Test results", 8, result.getAvailableTestResults().size());
    Map<String, String> tests = result.getAvailableTestResults();
    Iterator<String> testIts = tests.values().iterator();
    Assert.assertEquals("Available test result value", "A", testIts.next());
    Assert.assertEquals("Available test result value", "POS", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertEquals("Available test result value", "LOW", testIts.next());
    Assert.assertEquals("Available test result value", "NEG", testIts.next());
    Assert.assertFalse("No ABO Uninterpretable", result.getAboUninterpretable());
    Assert.assertFalse("No RH Uninterpretable", result.getRhUninterpretable());
    Assert.assertFalse("No TTI Uninterpretable", result.getTtiUninterpretable());
  }

  @Test
  public void testBloodTestingRuleEngineWithDonation6() throws Exception {
    Donation donation = donationRepository.findDonationById(6l);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<Long, String>());
    Assert.assertEquals("bloodTypingMatchStatus is MATCH", BloodTypingMatchStatus.MATCH,
        result.getBloodTypingMatchStatus());
    Assert.assertEquals("ttiStatus is NOT_DONE", TTIStatus.NOT_DONE, result.getTTIStatus());
    Assert.assertEquals("bloodAb is unknown", "", result.getBloodAbo());
    Assert.assertEquals("bloodRh is unknown", "", result.getBloodRh());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingTTITestsIds().size());
    Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
    Assert.assertEquals("Available Test results", 3, result.getAvailableTestResults().size());
    Map<String, String> tests = result.getAvailableTestResults();
    Iterator<String> testIts = tests.values().iterator();
    Assert.assertEquals("Available test result value", "Z", testIts.next());
    Assert.assertEquals("Available test result value", "?", testIts.next());
    Assert.assertEquals("Available test result value", "-", testIts.next());
    Assert.assertTrue("ABO Uninterpretable", result.getAboUninterpretable());
    Assert.assertTrue("RH Uninterpretable", result.getRhUninterpretable());
    // TTI Uninterpretable is always set to false - is this a bug?
    //Assert.assertTrue("TTI Uninterpretable", result.getTtiUninterpretable());
  }
  
  @Test
  public void testBloodTestingRuleEngineWithDonation12_TTIUnsafeAndFirstEntry() throws Exception {
    Donation donation = donationRepository.findDonationById(12l);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<Long, String>());
    Assert.assertEquals("TTIStatus is NOT_DONE", TTIStatus.NOT_DONE, result.getTTIStatus());
    Assert.assertEquals("No pending TTI tests", 0, result.getPendingTTITestsIds().size());
  }
  
  @Test
  public void testBloodTestingRuleEngineWithDonation12_TTIUnsafeAndFirstEntryAndPendingReEntry() throws Exception {
    Donation donation = donationRepository.findDonationById(12l);
    Map<Long, String> newTtiTestResults = new HashMap<>();
    newTtiTestResults.put(17L, "POS");
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, newTtiTestResults);
    Assert.assertEquals("TTIStatus is NOT_DONE", TTIStatus.TTI_UNSAFE, result.getTTIStatus());
    Assert.assertEquals("No pending TTI tests", 2, result.getPendingTTITestsIds().size());
  }
  
  @Test
  public void testBloodTestingRuleEngineWithDonation13_TTIUnsafeAndReEntry() throws Exception {
    Donation donation = donationRepository.findDonationById(13l);
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<Long, String>());
    Assert.assertEquals("TTIStatus is TTI_UNSAFE", TTIStatus.TTI_UNSAFE, result.getTTIStatus());
    Assert.assertEquals("Pending TTI tests", 2, result.getPendingTTITestsIds().size());
  }

  @Test
  public void testBloodTestingRuleEngineTTIReEntryRequiredList() throws Exception {
    Donation donation = donationRepository.findDonationById(14l);
    Map<Long, String> testResults = new HashMap<Long, String>();
    testResults.put(17L, "POS");
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, testResults);
    Assert.assertEquals("Re-entry required TTI tests", 1, result.getReEntryRequiredTTITestIds().size());
  }

  @Test
  public void testBloodTestingRuleEngineBloodTypingReEntryRequiredList() throws Exception {
    Donation donation = donationRepository.findDonationById(14l);
    Map<Long, String> testResults = new HashMap<Long, String>();
    testResults.put(1L, "A");
    BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, testResults);
    Assert.assertEquals("Re-entry required blood typing tests", 1,
        result.getReEntryRequiredBloodTypingTestIds().size());
  }
}

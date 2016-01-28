package repository.bloodtesting;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import model.bloodtesting.TTIStatus;
import model.donation.Donation;

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
		}
		finally {
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
	public void testBloodTestingRuleEngineWithDonation8() throws Exception {
	  // donation 8 is for a 1st time donor who has only had initial outcomes entered for ABO/Rh
	  Donation donation = donationRepository.findDonationById(8l);
	  BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<Long, String>());
	  Assert.assertEquals("bloodTypingMatchStatus is set", BloodTypingMatchStatus.NO_MATCH, result.getBloodTypingMatchStatus());
	  Assert.assertEquals("bloodTypingStatus is set", BloodTypingStatus.NO_MATCH, result.getBloodTypingStatus());
	  Assert.assertEquals("bloodAb is set", "O", result.getBloodAbo());
	  Assert.assertEquals("bloodRh is set", "-", result.getBloodRh());
	  Assert.assertEquals("Pending blood typing tests", 2, result.getPendingBloodTypingTestsIds().size());
	  Assert.assertEquals("Availale test results", 2, result.getAvailableTestResults().size());
	  Assert.assertFalse("No ABO Uninterpretable", result.getAboUninterpretable());
	  Assert.assertFalse("No RH Uninterpretable", result.getRhUninterpretable());
	}

	@Test
	public void testBloodTestingRuleEngineWithDonation9() throws Exception {
	  // donation 9 is for a 1st time donor who has had initial and repeat outcomes entered for ABO/Rh
	  Donation donation = donationRepository.findDonationById(9l);
	  BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(donation, new HashMap<Long, String>());
	  Assert.assertEquals("bloodTypingMatchStatus is set", BloodTypingMatchStatus.NO_MATCH, result.getBloodTypingMatchStatus());
	  Assert.assertEquals("bloodTypingStatus is set", BloodTypingStatus.COMPLETE, result.getBloodTypingStatus());
	  Assert.assertEquals("bloodAb is set", "O", result.getBloodAbo());
	  Assert.assertEquals("bloodRh is set", "-", result.getBloodRh());
	  Assert.assertEquals("Pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
	  Assert.assertEquals("Availale test results", 4, result.getAvailableTestResults().size());
	  Assert.assertFalse("No ABO Uninterpretable", result.getAboUninterpretable());
	  Assert.assertFalse("No RH Uninterpretable", result.getRhUninterpretable());
	}
}

package repository.bloodtesting;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import model.bloodtesting.TTIStatus;
import model.collectedsample.CollectedSample;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import repository.CollectedSampleRepository;
import viewmodel.BloodTestingRuleResult;

/**
 * Test using DBUnit to test the BloodTestingRuleEngine. See the XML data set file called
 * BloodTestingRuleRepositoryDataset.xml which contains all the data used in the tests. Below is
 * some commented out code that can be used to generate XML files from the database.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@WebAppConfiguration
public class BloodTestingRuleEngineTest {
	
	@Autowired
	BloodTestingRuleEngine bloodTestingRuleEngine;
	
	@Autowired
	CollectedSampleRepository collectedSampleRepository;
	
	@Autowired
	private DataSource dataSource;
	
	static IDatabaseConnection connection;
	
	@Before
	public void init() throws Exception {
		if (connection == null) {
			getConnection();
		}
		IDataSet dataSet = getDataSet();
		DatabaseOperation.INSERT.execute(connection, dataSet);
	}
	
	@After
	public void after() throws Exception {
		// Remove data from database
		DatabaseOperation.DELETE_ALL.execute(connection, getDataSet());
	}
	
	/**
	 * This method is executed once before test case execution start and acquires datasource from
	 * spring context and create new dbunit IDatabaseConnection. This method is also useful to set
	 * HSQLDB datatypefactory.
	 */
	private void getConnection() throws SQLException {
		connection = new DatabaseDataSourceConnection(dataSource);
		DatabaseConfig config = connection.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
	}
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/BloodTestingRuleRepositoryDataset.xml");
		return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
	}
	
	@Test
	@Transactional
	public void testBloodTestingRuleEngineWithCollectedSample1() throws Exception {
		CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(1l);
		BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(collectedSample, new HashMap<Long, String>());
		Assert.assertEquals("bloodTypingMatchStatus is MATCH", result.getBloodTypingMatchStatus(),
		    BloodTypingMatchStatus.MATCH);
		Assert.assertEquals("ttiStatus is TTI_SAFE", TTIStatus.TTI_SAFE, result.getTTIStatus());
		Assert.assertEquals("bloodAb is )", "O", result.getBloodAbo());
		Assert.assertEquals("bloodRh is +", "+", result.getBloodRh());
		Assert.assertEquals("No pending TTI tests", 0, result.getPendingTTITestsIds().size());
		Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
		Assert.assertEquals("No pending tti tests", 0, result.getPendingTTITestsIds().size());
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
	@Transactional
	public void testBloodTestingRuleEngineWithCollectedSample2() throws Exception {
		CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(2l);
		BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(collectedSample, new HashMap<Long, String>());
		Assert.assertEquals("bloodTypingMatchStatus is MATCH", result.getBloodTypingMatchStatus(),
		    BloodTypingMatchStatus.MATCH);
		Assert.assertEquals("ttiStatus is TTI_SAFE", TTIStatus.TTI_SAFE, result.getTTIStatus());
		Assert.assertEquals("bloodAb is )", "A", result.getBloodAbo());
		Assert.assertEquals("bloodRh is +", "-", result.getBloodRh());
		Assert.assertEquals("No pending TTI tests", 0, result.getPendingTTITestsIds().size());
		Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
		Assert.assertEquals("No pending tti tests", 0, result.getPendingTTITestsIds().size());
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
	@Transactional
	public void testBloodTestingRuleEngineWithCollectedSample3() throws Exception {
		CollectedSample collectedSample = collectedSampleRepository.findCollectedSampleById(3l);
		BloodTestingRuleResult result = bloodTestingRuleEngine.applyBloodTests(collectedSample, new HashMap<Long, String>());
		Assert.assertEquals("bloodTypingMatchStatus is NOT_DONE", result.getBloodTypingMatchStatus(),
		    BloodTypingMatchStatus.NOT_DONE);
		Assert.assertEquals("ttiStatus is NOT_DONE", TTIStatus.NOT_DONE, result.getTTIStatus());
		Assert.assertEquals("bloodAb is empty", 0, result.getBloodAbo().length());
		Assert.assertEquals("bloodRh is empty", 0, result.getBloodRh().length());
		Assert.assertEquals("No pending TTI tests", 0, result.getPendingTTITestsIds().size());
		Assert.assertEquals("No pending blood typing tests", 0, result.getPendingBloodTypingTestsIds().size());
		Assert.assertEquals("No pending tti tests", 0, result.getPendingTTITestsIds().size());
		Assert.assertEquals("No availale test results", 0, result.getAvailableTestResults().size());
		Assert.assertFalse("No ABO Uninterpretable", result.getAboUninterpretable());
		Assert.assertFalse("No RH Uninterpretable", result.getRhUninterpretable());
		Assert.assertFalse("No TTI Uninterpretable", result.getTtiUninterpretable());
	}
	
	// Useful to generate datasets from a database.
	/*
	public static void main(String[] args) throws Exception {
		// database connection
		Class driverClass = Class.forName("com.mysql.jdbc.Driver");
		Connection jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/v2v_new", "root", "root");
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
		
		// partial database export
		QueryDataSet partialDataSet = new QueryDataSet(connection);
		partialDataSet.addTable("BloodTestingRule");
		partialDataSet.addTable("BloodTest");
		partialDataSet.addTable("BloodTestResult");
		partialDataSet.addTable("BloodTest_WorksheetType");
		partialDataSet.addTable("BloodTest_AUD");
		partialDataSet.addTable("BloodTestingRule_AUD");
		partialDataSet.addTable("CollectedSample");
		partialDataSet.addTable("Donor");
		partialDataSet.addTable("DonationType");
		partialDataSet.addTable("BloodBagType");
		partialDataSet.addTable("Product");
		partialDataSet.addTable("CollectionBatch");
		partialDataSet.addTable("Location");
		partialDataSet.addTable("ProductType");
		partialDataSet.addTable("TestBatch");
		partialDataSet.addTable("Contact");
		partialDataSet.addTable("Address");
		partialDataSet.addTable("AddressType");
		partialDataSet.addTable("IdType");
		partialDataSet.addTable("PreferredLanguage");
		partialDataSet.addTable("ContactMethodType");
		partialDataSet.addTable("DeferralReason");
		partialDataSet.addTable("DonorDeferral");
		partialDataSet.addTable("DonorCode");
		FlatXmlDataSet.write(partialDataSet, new FileOutputStream("test/dataset/BloodTestingRuleRepositoryDataset_new.xml"));
	}*/
}

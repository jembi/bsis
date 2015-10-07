package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import model.admin.DataType;
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

import repository.bloodtesting.BloodTestingRepository;
import viewmodel.BloodTestingRuleResult;

/**
 * Test using DBUnit to test the BloodTesting Repository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class BloodTestingRepositoryTest {
	
	@Autowired
	BloodTestingRepository bloodTestingRepository;
	
	@Autowired
	DonationRepository donationRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/BloodTestingRepositoryDataset.xml");
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
	
	/*
	<BloodTestResult id="1" createdDate="2015-02-06 02:09:13.0"
		lastUpdated="2015-02-06 02:09:13.0" notes="" result="NEG"
		testedOn="2015-02-06 02:09:13.0" bloodTest_id="17" donation_id="1"
		createdBy_id="1" lastUpdatedBy_id="1" />
	<BloodTestResult id="2" createdDate="2015-02-06 02:09:13.0"
		lastUpdated="2015-02-06 02:09:13.0" notes="" result="NEG"
		testedOn="2015-02-06 02:09:13.0" bloodTest_id="20" donation_id="1"
		createdBy_id="1" lastUpdatedBy_id="1" />
	<BloodTestResult id="3" createdDate="2015-02-06 02:09:13.0"
		lastUpdated="2015-02-06 02:09:13.0" notes="" result="NEG"
		testedOn="2015-02-06 02:09:13.0" bloodTest_id="23" donation_id="1"
		createdBy_id="1" lastUpdatedBy_id="1" />
	<BloodTestResult id="4" createdDate="2015-02-06 02:09:13.0"
		lastUpdated="2015-02-06 02:09:13.0" notes="" result="NEG"
		testedOn="2015-02-06 02:09:13.0" bloodTest_id="26" donation_id="1"
		createdBy_id="1" lastUpdatedBy_id="1" />
	<BloodTestResult id="5" createdDate="2015-02-06 02:09:23.0"
		lastUpdated="2015-02-06 02:09:23.0" notes="" result="O"
		testedOn="2015-02-06 02:09:23.0" bloodTest_id="1" donation_id="1"
		createdBy_id="1" lastUpdatedBy_id="1" />
	<BloodTestResult id="6" createdDate="2015-02-06 02:09:23.0"
		lastUpdated="2015-02-06 02:09:23.0" notes="" result="POS"
		testedOn="2015-02-06 02:09:23.0" bloodTest_id="2" donation_id="1"
		createdBy_id="1" lastUpdatedBy_id="1" />
	 */
	
	@Test
	public void testGetAll() throws Exception {
		Donation donation1 = donationRepository.findDonationById(1l);
	
		//List<DataType> all = bloodTestingRepository.saveBloodTestResultsToDatabase(bloodTestResultsForDonation,
		//	donation1, testedOn,ruleResult);
	}
}

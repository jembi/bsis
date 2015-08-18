package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import model.bloodtesting.BloodTestContext;

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

/**
 * Test using DBUnit to test the GenericConfigRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class GenericConfigRepositoryTest {
	
	@Autowired
	GenericConfigRepository genericConfigRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/GenericConfigRepositoryDataset.xml");
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
	public void testGetConfigProperties() throws Exception {
		Map<String, String> all = genericConfigRepository.getConfigProperties("labsetup");
		Assert.assertNotNull("There are GenericConfigs", all);
		Assert.assertEquals("There are 22 labsetup GenericConfig", 22, all.size());
		Assert.assertTrue("The labsetup GenericConfig contains crossmatchProcedure",
		    all.keySet().contains("crossmatchProcedure"));
	}
	
	@Test
	public void testFindGenericConfigById() throws Exception {
		List<String> propertyOwners = new ArrayList<String>();
		propertyOwners.add("donationRequirements");
		propertyOwners.add("productReleaseRequirements");
		Map<String, String> all = genericConfigRepository.getConfigProperties(propertyOwners);
		Assert.assertNotNull("There are GenericConfigs", all);
		Assert.assertEquals("There are 10 GenericConfig", 10, all.size());
		Assert.assertTrue("The GenericConfig contains donorRecordRequired", all.keySet().contains("donorRecordRequired"));
		Assert.assertTrue("The GenericConfig contains daysBetweenConsecutiveDonations",
		    all.keySet().contains("daysBetweenConsecutiveDonations"));
	}
	
	@Test
	public void testFindGenericConfigByIdUnknown() throws Exception {
		Map<String, String> all = genericConfigRepository.getConfigProperties("junit");
		Assert.assertNotNull("Does not return a null list", all);
		Assert.assertTrue("It is an empty list", all.isEmpty());
	}
	
	@Test
	public void testGetCurrentBloodTypingContext() throws Exception {
		BloodTestContext context = genericConfigRepository.getCurrentBloodTypingContext();
		Assert.assertNotNull("BloodTestContext was found", context);
		Assert.assertEquals(BloodTestContext.RECORD_BLOOD_TYPING_TESTS, context);
	}
	
	@Test
	public void testUpdate() throws Exception {
		Map<String, String> all = genericConfigRepository.getConfigProperties("labsetup");
		Assert.assertNotNull("There are GenericConfigs", all);
		String value = all.get("recordUsage");
		value = "false";
		all.put("recordUsage", value);
		genericConfigRepository.updateConfigProperties("labsetup", all);
		Map<String, String> allSaved = genericConfigRepository.getConfigProperties("labsetup");
		String updatedValue = allSaved.get("recordUsage");
		Assert.assertEquals("recordUsage is false", "false", updatedValue);
	}
	
	@Test
	public void testUpdateWorksheetProperties() throws Exception {
		Map<String, String> all = genericConfigRepository.getConfigProperties("donationsWorksheet");
		Assert.assertNotNull("There are GenericConfigs", all);
		String value = all.get("rowHeight");
		value = "40";
		all.put("rowHeight", value);
		genericConfigRepository.updateWorksheetProperties(all);
		Map<String, String> allSaved = genericConfigRepository.getConfigProperties("donationsWorksheet");
		String updatedValue = allSaved.get("rowHeight");
		Assert.assertEquals("recordUsage is 40", "40", updatedValue);
	}
}

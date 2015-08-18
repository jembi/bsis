package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import model.usage.ComponentUsage;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test using DBUnit to test the UsageRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@WebAppConfiguration
public class UsageRepositoryTest {
	
	@Autowired
	UsageRepository usageRepository;
	
	@Autowired
	ComponentRepository componentRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/UsageRepositoryDataset.xml");
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
	@Transactional
	public void testFindUsageById() throws Exception {
		ComponentUsage one = usageRepository.findUsageById(1l);
		Assert.assertNotNull("There is a ComponentUsage", one);
		Assert.assertEquals("ComponentUsage is correct", "Lucky", one.getPatientName());
	}
	
	@Test
	@Transactional
	@Ignore("Bug - error in HSQL: could not resolve property: productNumber of: model.usage.ComponentUsage")
	public void testFindComponentUsage() throws Exception {
		usageRepository.findComponentUsage("1234");
	}
	
	@Test
	@Transactional
	@Ignore("Bug - error in HSQL: could not resolve property: productNumber of: model.usage.ComponentUsage")
	public void testFindUsageByProductNumber() throws Exception {
		usageRepository.findUsageByProductNumber("1234");
	}
	
	@Test
	@Transactional
	public void testFindUsageByIdUnknown() throws Exception {
		ComponentUsage one = usageRepository.findUsageById(123l);
		Assert.assertNull("There is no ComponentUsage", one);
	}
	
	@Test
	@Transactional
	public void testUpdateUsage() throws Exception {
		ComponentUsage one = usageRepository.findUsageById(1l);
		one.setHospital("Junit hospital");
		usageRepository.saveUsage(one);
		ComponentUsage savedOne = usageRepository.findUsageById(1l);
		Assert.assertEquals("ComponentUsage is updated", "Junit hospital", savedOne.getHospital());
	}
	
	@Test
	@Transactional
	public void testAddUsage() throws Exception {
		ComponentUsage one = new ComponentUsage();
		one.setHospital("Junit hospital");
		one.setComponent(componentRepository.findComponent(6l)); // note: this component is actually discarded
		one.setUsageDate(new Date());
		ComponentUsage savedOne = usageRepository.addUsage(one);
		Assert.assertNotNull("Saved ComponentUsage has an id", savedOne.getId());
		ComponentUsage retrievedOne = usageRepository.findUsageById(savedOne.getId());
		Assert.assertNotNull("There is a ComponentUsage", retrievedOne);
		Assert.assertEquals("ComponentUsage is updated", "Junit hospital", retrievedOne.getHospital());
	}
	
	@Test
	@Transactional
	@Ignore("Bug - error in HSQL: could not resolve property: productNumber of: model.usage.ComponentUsage")
	public void testDeleteUsage() throws Exception {
		usageRepository.deleteUsage("1234");
		ComponentUsage one = usageRepository.findUsageById(1l);
		Assert.assertNotNull("There is a ComponentUsage", one);
		Assert.assertTrue("ComponentUsage is deleted", one.getIsDeleted());
	}
	
	@Test
	@Transactional
	public void testDeleteAllUsages() throws Exception {
		usageRepository.deleteAllUsages();
		ComponentUsage one = usageRepository.findUsageById(1l);
		Assert.assertNull("There is no ComponentUsage", one);
	}
	
	@Test
	@Transactional
	@Ignore("Bug - error in HSQL: could not resolve property: productNumber of: model.usage.ComponentUsage")
	public void testFindAnyUsageMatching() throws Exception {
		List<String> usages = new ArrayList<String>();
		usages.add("Operation");
		usages.add("Emergency");
		usageRepository.findAnyUsageMatching("1234", "2015-01-01", "2015-10-10", usages);
	}
}

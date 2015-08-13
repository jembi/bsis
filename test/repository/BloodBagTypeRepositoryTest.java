package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import model.bloodbagtype.BloodBagType;

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
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test using DBUnit to test the BloodBagTypeRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@WebAppConfiguration
public class BloodBagTypeRepositoryTest {
	
	@Autowired
	BloodBagTypeRepository bloodBagTypeRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/BloodBagTypeRepositoryDataset.xml");
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
	public void testGetAllBloodBagTypes() throws Exception {
		List<BloodBagType> all = bloodBagTypeRepository.getAllBloodBagTypes();
		Assert.assertNotNull("There are blood bag types defined", all);
		Assert.assertEquals("There are 8 blood bag types defined", 8, all.size());
	}
	
	@Test
	@Transactional
	public void testFindBloodBagTypeByName() throws Exception {
		BloodBagType one = bloodBagTypeRepository.findBloodBagTypeByName("Single");
		Assert.assertNotNull("There is a blood bag types named 'Single'", one);
		Assert.assertEquals("There is a blood bag types named 'Single'", "Single", one.getBloodBagType());
	}
	
	@Test
	@Transactional
	public void testUpdateBloodBagType() throws Exception {
		BloodBagType two = bloodBagTypeRepository.findBloodBagTypeByName("Double");
		Assert.assertNotNull("There is a blood bag types named 'Double'", two);
		two.setCountAsDonation(false);
		two.setPeriodBetweenDonations(0);
		bloodBagTypeRepository.updateBloodBagType(two);
		BloodBagType savedTwo = bloodBagTypeRepository.findBloodBagTypeByName("Double");
		Assert.assertNotNull("There is a blood bag types named 'Double'", savedTwo);
		Assert.assertEquals("Is not counted as a donation", false, savedTwo.getCountAsDonation());
		Assert.assertEquals("0 days between donations", new Integer(0), savedTwo.getPeriodBetweenDonations());
	}
	
	@Test
	@Transactional
	public void testSaveBloodBagType() throws Exception {
		BloodBagType toBeSaved = new BloodBagType();
		toBeSaved.setBloodBagType("Junit");
		toBeSaved.setPeriodBetweenDonations(123);
		toBeSaved.setCountAsDonation(true);
		
		bloodBagTypeRepository.saveBloodBagType(toBeSaved);
		
		BloodBagType saved = bloodBagTypeRepository.findBloodBagTypeByName("Junit");
		Assert.assertNotNull("There is a blood bag types named 'Double'", saved);
		Assert.assertEquals("Is counted as a donation", true, saved.getCountAsDonation());
		Assert.assertEquals("123 days between donations", new Integer(123), saved.getPeriodBetweenDonations());
	}
}

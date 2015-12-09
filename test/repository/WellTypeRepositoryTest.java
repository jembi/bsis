package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import model.bloodtesting.WellType;

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
 * Test using DBUnit to test the WellTypeRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class WellTypeRepositoryTest {
	
	@Autowired
	WellTypeRepository wellTypeRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/WellTypeRepositoryDataset.xml");
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
	public void testGetAll() throws Exception {
		List<WellType> all = wellTypeRepository.getAllWellTypes();
		Assert.assertNotNull("There are WellTypes defined", all);
		Assert.assertEquals("There are 4 WellTypes defined", 4, all.size());
	}
	
	@Test
	public void testIsWellTypeValid() throws Exception {
		boolean valid = wellTypeRepository.isWellTypeValid("Sample");
		Assert.assertTrue("There is a matching WellType", valid);
	}
	
	@Test
	public void testIsWellTypeValidFalse() throws Exception {
		boolean valid = wellTypeRepository.isWellTypeValid("Junit");
		Assert.assertFalse("There is no matching WellType", valid);
	}
	
	@Test
	public void testGetWellTypeById() throws Exception {
		WellType one = wellTypeRepository.getWellTypeById(1);
		Assert.assertNotNull("There is a WellType defined", one);
		Assert.assertEquals("WellType is correct", "Sample", one.getWellType());
	}
	
	@Test
	public void testGetWellTypeByIdUnknown() throws Exception {
		WellType one = wellTypeRepository.getWellTypeById(123);
		Assert.assertNull("There is no WellType defined", one);
	}
	
	@Test
	public void testSaveAllWellTypesUpdate() throws Exception {
		WellType oldOne = wellTypeRepository.getWellTypeById(1);
		oldOne.setWellType("oldSample");
		List<WellType> all = new ArrayList<>();
		all.add(oldOne);
		wellTypeRepository.saveAllWellTypes(all);
		WellType savedOldOne = wellTypeRepository.getWellTypeById(1);
		Assert.assertEquals("WellType is updated", "oldSample", savedOldOne.getWellType());
	}
	
	@Test
	public void testSaveAllWellTypesAdd() throws Exception {
		WellType newOne = new WellType();
		newOne.setRequiresSample(true);
		newOne.setWellType("Junit");
		newOne.setIsDeleted(false);
		List<WellType> all = new ArrayList<>();
		all.add(newOne);
		wellTypeRepository.saveAllWellTypes(all);
		List<WellType> allSaved = wellTypeRepository.getAllWellTypes();
		Assert.assertEquals("There are 5 WellTypes defined", 5, allSaved.size());
	}
}

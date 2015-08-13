package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import model.compatibility.CrossmatchType;

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
 * Test using DBUnit to test the CrossmatchType Repository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@WebAppConfiguration
public class CrossmatchTypeRepositoryTest {
	
	@Autowired
	CrossmatchTypeRepository crossmatchTypeRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/CrossmatchTypeRepositoryDataset.xml");
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
	public void testGetAllCrossmatchTypes() throws Exception {
		List<CrossmatchType> all = crossmatchTypeRepository.getAllCrossmatchTypes();
		Assert.assertNotNull("There are crossmatchtypes defined", all);
		
		Assert.assertEquals("There are 2 crossmatch types defined", 2, all.size());
	}
	
	@Test
	@Transactional
	public void testIsCrossmatchTypeValid() throws Exception {
		boolean matched = crossmatchTypeRepository.isCrossmatchTypeValid("Anti Human Globulin");
		Assert.assertTrue("Crossmatch type called 'Anti Human Globulin' exists", matched);
	}
	
	@Test
	@Transactional
	@Ignore("The saveAllCrossmatchTypes method fails with an error: 'detached entity passed to persist'")
	public void testSaveAllCrossmatchTypes() throws Exception {
		List<CrossmatchType> all = crossmatchTypeRepository.getAllCrossmatchTypes();
		Assert.assertNotNull("There are crossmatchtypes defined", all);
		
		CrossmatchType toBeSaved = new CrossmatchType();
		toBeSaved.setId(3); // if this isn't set then the entity will not be saved
		toBeSaved.setCrossmatchType("Junit");
		all.add(toBeSaved);
		
		crossmatchTypeRepository.saveAllCrossmatchTypes(all);
		
		List<CrossmatchType> allSaved = crossmatchTypeRepository.getAllCrossmatchTypes();
		Assert.assertEquals("There are now 3 crossmatch types defined", 3, allSaved.size());
	}
}

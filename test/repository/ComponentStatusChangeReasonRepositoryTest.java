package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import model.componentmovement.ComponentStatusChangeReason;
import model.componentmovement.ComponentStatusChangeReasonCategory;

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
 * Test using DBUnit to test the ComponentStatusChangeReasonRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@WebAppConfiguration
public class ComponentStatusChangeReasonRepositoryTest {
	
	@Autowired
	ComponentStatusChangeReasonRepository componentStatusChangeReasonRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/ComponentStatusChangeReasonRepositoryDataset.xml");
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
	public void testGetAll() throws Exception {
		List<ComponentStatusChangeReason> all = componentStatusChangeReasonRepository.getAllComponentStatusChangeReasons();
		Assert.assertNotNull("There are ComponentStatusChangeReason", all);
		Assert.assertEquals("There are 11 ComponentStatusChangeReason", 11, all.size());
	}
	
	@Test
	@Transactional
	public void testGetComponentStatusChangeReasons() throws Exception {
		List<ComponentStatusChangeReason> all = componentStatusChangeReasonRepository
		        .getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.DISCARDED);
		Assert.assertNotNull("There are DISCARDED ComponentStatusChangeReason", all);
		Assert.assertEquals("There are 6 DISCARDED ComponentStatusChangeReason", 6, all.size());
	}
	
	@Test
	@Transactional
	public void testGetComponentStatusChangeReasonsNone() throws Exception {
		List<ComponentStatusChangeReason> all = componentStatusChangeReasonRepository
		        .getComponentStatusChangeReasons(ComponentStatusChangeReasonCategory.ISSUED);
		Assert.assertNotNull("Doesn't return a null list", all);
		Assert.assertEquals("There are 0 ISSUED ComponentStatusChangeReason", 0, all.size());
	}
	
	@Test
	@Transactional
	public void testGetComponentStatusChangeReasonById() throws Exception {
		ComponentStatusChangeReason one = componentStatusChangeReasonRepository.getComponentStatusChangeReasonById(1);
		Assert.assertNotNull("There is a ComponentStatusChangeReason", one);
		Assert.assertEquals("ComponentStatusChangeReason matches", ComponentStatusChangeReasonCategory.DISCARDED,
		    one.getCategory());
	}
	
	@Test
	@Transactional
	public void testGetComponentStatusChangeReasonByIdUnknown() throws Exception {
		ComponentStatusChangeReason one = componentStatusChangeReasonRepository.getComponentStatusChangeReasonById(123);
		Assert.assertNull("There is no ComponentStatusChangeReason", one);
	}
	
	@Test
	@Transactional
	@Ignore("Bug - getAllComponentStatusChangeReasonsAsMap only returns one ComponentStatusChangeReason per ComponentStatusChangeReasonCategory")
	public void testGetAllComponentStatusChangeReasonsAsMap() throws Exception {
		Map<ComponentStatusChangeReasonCategory, ComponentStatusChangeReason> result = componentStatusChangeReasonRepository
		        .getAllComponentStatusChangeReasonsAsMap();
		Assert.assertNotNull("Does not return a null map", result);
		Assert.assertEquals("There are 3 ComponentStatusChangeReasonCategory with ComponentStatusChangeReason", 3, result.size());
	}
}

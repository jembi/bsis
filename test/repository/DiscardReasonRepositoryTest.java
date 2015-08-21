package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test using DBUnit to test the DiscardReasonRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class DiscardReasonRepositoryTest {
	
	@Autowired
	DiscardReasonRepository discardReasonRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/DiscardReasonRepositoryDataset.xml");
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
		List<ComponentStatusChangeReason> all = discardReasonRepository.getAllDiscardReasons();
		Assert.assertNotNull("There are discard reasons defined", all);
		Assert.assertEquals("There are 6 discard reasons defined", 6, all.size());
	}
	
	@Test
	public void testGetDiscardReasonById() throws Exception {
		ComponentStatusChangeReason discardReason = discardReasonRepository.getDiscardReasonById(1);
		Assert.assertNotNull("Discard reason with id 1 exists", discardReason);
	}
	
	@Test
	public void testFindDeferralReason() throws Exception {
		ComponentStatusChangeReason reason = discardReasonRepository.findDiscardReason("Incomplete Donation");
		Assert.assertNotNull("Discard reason exists", reason);
		Assert.assertEquals("Discard reason matches", "Incomplete Donation", reason.getStatusChangeReason());
	}
	
	@Test
	public void testFindDeferralReasonUnknown() throws Exception {
		ComponentStatusChangeReason reason = discardReasonRepository.findDiscardReason("Junit");
		Assert.assertNull("Discard reason does not exist", reason);
	}
	
	@Test
	public void testUpdateDeferralReason() throws Exception {
		ComponentStatusChangeReason reason = discardReasonRepository.getDiscardReasonById(1);
		Assert.assertNotNull("Discard reason exists", reason);
		
		reason.setStatusChangeReason("Junit");
		discardReasonRepository.updateDiscardReason(reason);
		
		ComponentStatusChangeReason savedReason = discardReasonRepository.getDiscardReasonById(1);
		Assert.assertNotNull("Discard reason still exists", savedReason);
		Assert.assertEquals("Reason has been updated", "Junit", savedReason.getStatusChangeReason());
	}
	
	@Test
	public void testSaveDeferralReason() throws Exception {
		ComponentStatusChangeReason reason = new ComponentStatusChangeReason();
		reason.setStatusChangeReason("Junit");
		reason.setCategory(ComponentStatusChangeReasonCategory.DISCARDED);
		discardReasonRepository.saveDiscardReason(reason);
		
		List<ComponentStatusChangeReason> all = discardReasonRepository.getAllDiscardReasons();
		Assert.assertNotNull("There are Discard reasons defined", all);
		Assert.assertEquals("There are 7 Discard reasons defined", 7, all.size());
	}
}

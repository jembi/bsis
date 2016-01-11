package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import model.donordeferral.DeferralReason;

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
 * Test using DBUnit to test the DeferralReason Repository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class DeferralReasonRepositoryTest {
	
	@Autowired
	DeferralReasonRepository deferralReasonRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/DeferralReasonRepositoryDataset.xml");
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
		List<DeferralReason> all = deferralReasonRepository.getAllDeferralReasons();
		Assert.assertNotNull("There are deferral reasons defined", all);
		Assert.assertEquals("There are 6 deferral reasons defined", 6, all.size());
	}
	
	@Test
	public void testGetDeferralReasonById() throws Exception {
		DeferralReason deferralReason = deferralReasonRepository.getDeferralReasonById(1l);
		Assert.assertNotNull("DeferralReason with id 1 exists", deferralReason);
	}
	
	@Test
	public void testFindDeferralReason() throws Exception {
		DeferralReason deferralReason = deferralReasonRepository.findDeferralReason("High risk behaviour");
		Assert.assertNotNull("DeferralReason exists", deferralReason);
		Assert.assertEquals("Deferral reason matches", "High risk behaviour", deferralReason.getReason());
	}
	
	@Test
	public void testFindDeferralReasonUnknown() throws Exception {
		DeferralReason deferralReason = deferralReasonRepository.findDeferralReason("Junit");
		Assert.assertNull("DeferralReason does not exist", deferralReason);
	}
	
	@Test
	public void testUpdateDeferralReason() throws Exception {
		DeferralReason deferralReason = deferralReasonRepository.getDeferralReasonById(1l);
		Assert.assertNotNull("DeferralReason exists", deferralReason);
		
		deferralReason.setReason("Junit");
		deferralReasonRepository.updateDeferralReason(deferralReason);
		
		DeferralReason savedDeferralReason = deferralReasonRepository.getDeferralReasonById(1l);
		Assert.assertNotNull("DeferralReason still exists", savedDeferralReason);
		Assert.assertEquals("Reason has been updated", "Junit", savedDeferralReason.getReason());
	}
	
	@Test
	public void testSaveDeferralReason() throws Exception {
		DeferralReason deferralReason = new DeferralReason();
		deferralReason.setReason("New reason");
		deferralReasonRepository.saveDeferralReason(deferralReason);
		
		List<DeferralReason> all = deferralReasonRepository.getAllDeferralReasons();
		Assert.assertNotNull("There are deferral reasons defined", all);
		Assert.assertEquals("There are 7 deferral reasons defined", 7, all.size());
	}
}

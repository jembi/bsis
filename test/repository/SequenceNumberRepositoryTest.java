package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test using DBUnit to test the SequenceNumberRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@WebAppConfiguration
public class SequenceNumberRepositoryTest {
	
	@Autowired
	SequenceNumberRepository sequenceNumberRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/SequenceNumberRepositoryDataset.xml");
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
	
	@After
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
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testGetNextDonationIdentificationNumber() throws Exception {
		String next = sequenceNumberRepository.getNextDonationIdentificationNumber();
		Assert.assertEquals("next DIN is correct", "C000003", next);
		next = sequenceNumberRepository.getNextDonationIdentificationNumber();
		Assert.assertEquals("next DIN is correct", "C000004", next);
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testGetNextRequestNumber() throws Exception {
		String next = sequenceNumberRepository.getNextRequestNumber();
		Assert.assertEquals("next RequestNumber is correct", "R000005", next);
		next = sequenceNumberRepository.getNextRequestNumber();
		Assert.assertEquals("next RequestNumber is correct", "R000006", next);
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testGetNextDonorNumber() throws Exception {
		String next = sequenceNumberRepository.getNextDonorNumber();
		Assert.assertEquals("next DonorNumber is correct", "000003", next);
		next = sequenceNumberRepository.getNextDonorNumber();
		Assert.assertEquals("next DonorNumber is correct", "000004", next);
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testGetBatchDonationIdentificationNumbers() throws Exception {
		List<String> next = sequenceNumberRepository.getBatchDonationIdentificationNumbers(25);
		Assert.assertNotNull("Next batch DINs exist", next);
		Assert.assertEquals("next batch DINs are correct", 25, next.size());
		Assert.assertEquals("next batch DINs are correct", "C000003", next.get(0));
		Assert.assertEquals("next batch DINs are correct", "C000004", next.get(1));
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testGetBatchRequestNumbers() throws Exception {
		List<String> next = sequenceNumberRepository.getBatchRequestNumbers(2);
		Assert.assertNotNull("Next batch requestNumbers exist", next);
		Assert.assertEquals("next batch requestNumbers are correct", 2, next.size());
		Assert.assertEquals("next batch requestNumbers are correct", "R000005", next.get(0));
		Assert.assertEquals("next batch requestNumbers are correct", "R000006", next.get(1));
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testGetSequenceNumber() throws Exception {
		String next = sequenceNumberRepository.getSequenceNumber("Test", "testNumber");
		Assert.assertEquals("next test number is correct", "000023", next);
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testGetNextWorksheetBatchNumber() throws Exception {
		String next = sequenceNumberRepository.getNextWorksheetBatchNumber();
		Assert.assertEquals("next worksheet batchNumber is correct", "000001", next);
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testGetNextBatchNumber() throws Exception {
		String next = sequenceNumberRepository.getNextBatchNumber();
		Assert.assertEquals("next batchNumber is correct", "000002", next);
	}
	
	@Test
	@Transactional
	@Ignore("Because this test updates data and rollback is not working correctly, DBUnit hangs when cleaning up the database")
	public void testGetNextTestBatchNumber() throws Exception {
		String next = sequenceNumberRepository.getNextTestBatchNumber();
		Assert.assertEquals("next test batchNumber is correct", "000000", next);
		next = sequenceNumberRepository.getNextTestBatchNumber();
		Assert.assertEquals("next test batchNumber is correct", "000001", next);
	}
}

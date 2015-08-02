package repository;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import model.donation.Donation;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Test using DBUnit to test the Donation Repository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@WebAppConfiguration
public class DonationRepositoryTest {
	
	@Autowired
	DonationRepository donationRepository;
	
	@Autowired
	private DataSource dataSource;

	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/DonationRepositoryDataset.xml");
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
	public void testFindDonationById() throws Exception {
		Donation donation = donationRepository.findDonationById(1L);
		Assert.assertNotNull("There is a donation with id 1", donation);
		Assert.assertEquals("The donation has a DIN of 1234567", "1234567", donation.getDonationIdentificationNumber());
	}

	@Test
	@Transactional
	public void testGetAllDonations() throws Exception {
		List<Donation> all = donationRepository.getAllDonations();
		Assert.assertNotNull("There are donations", all);
		Assert.assertEquals("There are 3 donations", 3, all.size());
	}
	
	@Test
	@Transactional
	public void testGetDonations() throws Exception {
		Date start = new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-01");
		Date end = new SimpleDateFormat("yyyy-MM-dd").parse("2015-02-10");
		List<Donation> all = donationRepository.getDonations(start, end);
		Assert.assertNotNull("There are donations", all);
		Assert.assertEquals("There are 2 donations", 2, all.size());
	}
}

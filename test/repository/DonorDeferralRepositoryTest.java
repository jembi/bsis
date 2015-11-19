package repository;

import java.io.File;
import java.sql.SQLException;

import javax.sql.DataSource;

import model.donor.Donor;
import model.donordeferral.DonorDeferral;

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
 * Test using DBUnit to test the DonorDeferralRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class DonorDeferralRepositoryTest {
	
	@Autowired
	DonorDeferralRepository donorDeferralRepository;
	
	@Autowired
	DonorRepository donorRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/DonorDeferralRepositoryDataset.xml");
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
	public void testGetById() throws Exception {
		DonorDeferral deferral = donorDeferralRepository.findDonorDeferralById(1l);
		Assert.assertNotNull("There is a deferral", deferral);
		Assert.assertEquals("Correct deferral returned", "High risk behaviour", deferral.getDeferralReason().getReason());
	}
	
	@Test(expected = javax.persistence.NoResultException.class)
	public void testGetByIdDoesNotExist() throws Exception {
		donorDeferralRepository.findDonorDeferralById(123l);
	}
	
	@Test
	public void testCountDonorDeferralsForDonor() throws Exception {
		Donor donor = donorRepository.findDonorById(1l);
		int count = donorDeferralRepository.countDonorDeferralsForDonor(donor);
		Assert.assertEquals("Donor has 2 deferrals", 2, count); 
	}

}

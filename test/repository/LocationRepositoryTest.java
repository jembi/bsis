package repository;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import model.location.Location;

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
 * Test using DBUnit to test the LocationRepository
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:**/applicationContextTest.xml")
@Transactional
@WebAppConfiguration
public class LocationRepositoryTest {
	
	@Autowired
	LocationRepository locationRepository;
	
	@Autowired
	private DataSource dataSource;
	
	private IDataSet getDataSet() throws Exception {
		File file = new File("test/dataset/LocationRepositoryDataset.xml");
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
	public void testGetAllLocations() throws Exception {
		List<Location> all = locationRepository.getAllLocations();
		Assert.assertNotNull("There are Locations", all);
		Assert.assertEquals("There are 5 Locations", 5, all.size());
	}
	
	@Test
	public void testGetAllUsageSites() throws Exception {
		List<Location> all = locationRepository.getAllUsageSites();
		Assert.assertNotNull("There are usage site Locations", all);
		Assert.assertEquals("There are 2 usage site Location", 2, all.size());
	}
	
	@Test
	public void testGetAllVenues() throws Exception {
		List<Location> all = locationRepository.getAllVenues();
		Assert.assertNotNull("There are donor panel Locations", all);
		Assert.assertEquals("There are 3 donor panel Locations", 3, all.size());
	}
	
	@Test
	public void testGetLocation() throws Exception {
		Location one = locationRepository.getLocation(1l);
		Assert.assertNotNull("There is a Location", one);
		Assert.assertEquals("The Location matches", "Maseru", one.getName());
	}
	
	@Test(expected = javax.persistence.NoResultException.class)
	public void testGetLocationDeleted() throws Exception {
		Location one = locationRepository.getLocation(6l);
	}
	
	@Test
	public void testGetLocationByName() throws Exception {
		Location one = locationRepository.findLocationByName("Maseru");
		Assert.assertNotNull("There is a Location", one);
		Assert.assertEquals("The Location matches", "Maseru", one.getName());
	}
	
	@Test(expected = javax.persistence.NoResultException.class)
	public void testGetLocationByNameDeleted() throws Exception {
		Location one = locationRepository.findLocationByName("Hlotse");
	}
	
	@Test
	public void testGetAllUsageSitesAsString() throws Exception {
		List<String> all = locationRepository.getAllUsageSitesAsString();
		Assert.assertNotNull("Does not return a null list", all);
		Assert.assertEquals("There are two usage sites", 2, all.size());
		Assert.assertTrue("Leribe Hospital is in the list", all.contains("Leribe Hospital"));
	}
	
	@Test
	public void testGetIDByName() throws Exception {
		Long id = locationRepository.getIDByName("Maseru");
		Assert.assertNotNull("There is an ID", id);
		Assert.assertEquals("The id for Maseru is correct", new Long(1), id);
	}
	
	@Test
	public void testGetIDByNameUnknown() throws Exception {
		Long id = locationRepository.getIDByName("Ada");
		Assert.assertNotNull("There is an not null ID", id);
		Assert.assertEquals("The id for Ada is unknown", new Long(-1), id);
	}
	
	@Test
	public void testDeleteAll() throws Exception {
		locationRepository.deleteAllLocations();
		List<Location> all = locationRepository.getAllLocations();
		Assert.assertNotNull("Does not return an empty list", all);
		Assert.assertEquals("There are 0 Locations", 0, all.size());
	}
	
	@Test
	public void testDeleteLocation() throws Exception {
		List<Location> all1 = locationRepository.getAllVenues();
		locationRepository.deleteLocation(1L);
		List<Location> all2 = locationRepository.getAllVenues();
		Assert.assertEquals("Location has been deleted", all1.size() - 1, all2.size());
	}
	
	@Test
	public void testUpdateLocation() throws Exception {
		Location one = locationRepository.findLocationByName("Maseru");
		one.setIsMobileSite(true);
		locationRepository.updateLocation(1l, one);
		Location savedOne = locationRepository.findLocationByName("Maseru");
		Assert.assertTrue("The location is saved", savedOne.getIsMobileSite());
	}
	
	@Test
	public void testSave() throws Exception {
		Location one = new Location();
		one.setName("Clara");
		one.setIsDeleted(false);
		one.setIsMobileSite(true);
		one.setIsUsageSite(true);
		one.setIsVenue(false);
		locationRepository.saveLocation(one);
		Location savedOne = locationRepository.findLocationByName("Clara");
		Assert.assertEquals("The location is saved", "Clara", savedOne.getName());
	}
	
	@Test
	public void testSaveAll() throws Exception {
		Location one = new Location();
		one.setName("Clara");
		one.setIsDeleted(false);
		one.setIsMobileSite(true);
		one.setIsUsageSite(true);
		one.setIsVenue(false);
		Location two = locationRepository.findLocationByName("Maseru");
		two.setNotes("junit");
		List<Location> locations = new ArrayList<Location>();
		locations.add(one);
		locations.add(two);
		locationRepository.saveAllLocations(locations);
		Location savedOne = locationRepository.findLocationByName("Maseru");
		Assert.assertEquals("Maseru was updated", "junit", savedOne.getNotes());
		Location savedTwo = locationRepository.findLocationByName("Clara");
		Assert.assertNotNull("Is added", savedTwo);
	}
}

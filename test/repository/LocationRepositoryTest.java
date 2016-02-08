package repository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.location.Location;
import model.user.User;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import suites.DBUnitContextDependentTestSuite;

/**
 * Test using DBUnit to test the LocationRepository
 */
public class LocationRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  LocationRepository locationRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("test/dataset/LocationRepositoryDataset.xml");
    return new FlatXmlDataSetBuilder().setColumnSensing(true).build(file);
  }

  @Override
  protected User getLoggedInUser() throws Exception {
    return null;
  }

  @Test
  public void testGetAllLocations() throws Exception {
    List<Location> all = locationRepository.getAllLocations();
    Assert.assertNotNull("There are Locations", all);
    Assert.assertEquals("There are 7 Locations", 7, all.size());
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
    Assert.assertNotNull("There are venue Locations", all);
    Assert.assertEquals("There are 3 venue Locations", 3, all.size());
  }

  @Test
  public void testGetLocation() throws Exception {
    Location one = locationRepository.getLocation(1l);
    Assert.assertNotNull("There is a Location", one);
    Assert.assertEquals("The Location matches", "Maseru", one.getName());
  }

  @Test
  public void testGetLocationDeleted() throws Exception {
    Location one = locationRepository.getLocation(6l);
    Assert.assertNotNull("There is a Location", one);
    Assert.assertEquals("The Location matches", "Leribe Clinic", one.getName());
  }

  @Test
  public void testGetLocationByName() throws Exception {
    Location one = locationRepository.findLocationByName("Maseru");
    Assert.assertNotNull("There is a Location", one);
    Assert.assertEquals("The Location matches", "Maseru", one.getName());
  }

  @Test
  public void testGetLocationByNameDeleted() throws Exception {
    Location one = locationRepository.findLocationByName("Hlotse");
    Assert.assertNotNull("There is a Location", one);
    Assert.assertEquals("The Location matches", "Hlotse", one.getName());
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
    Location one = locationRepository.getLocation(1L);
    Assert.assertNotNull("There is a Location", one);
    Assert.assertEquals("The Location is marked as deleted", true, one.getIsDeleted());
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

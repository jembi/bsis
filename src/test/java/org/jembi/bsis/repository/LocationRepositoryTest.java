package org.jembi.bsis.repository;

import java.io.File;
import java.util.List;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.LocationRepository;
import org.jembi.bsis.suites.DBUnitContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test using DBUnit to test the LocationRepository
 */
public class LocationRepositoryTest extends DBUnitContextDependentTestSuite {

  @Autowired
  LocationRepository locationRepository;

  @Override
  protected IDataSet getDataSet() throws Exception {
    File file = new File("src/test/resources/dataset/LocationRepositoryDataset.xml");
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
    Assert.assertEquals("There are 8 Locations", 9, all.size());
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
  public void testDeleteLocation() throws Exception {
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
  public void testEntityExists() throws Exception {
    Assert.assertTrue("Location exists", locationRepository.verifyLocationExists(1L));
  }
  
  @Test
  public void testEntityDoesNotExist() throws Exception {
    Assert.assertFalse("Location does not exist", locationRepository.verifyLocationExists(123L));
  }

  @Test
  public void testGetLocationsByUsageSiteType() throws Exception {
    List<Location> all = locationRepository.getUsageSites();
    Assert.assertNotNull("There are usage site Locations", all);
    Assert.assertEquals("There are 2 usage site Location", 2, all.size());
  }

  @Test
  public void testGetLocationsByVenueType() throws Exception {
    List<Location> all = locationRepository.getVenues();
    Assert.assertNotNull("There are venue Locations", all);
    Assert.assertEquals("There are 3 venue Locations", 4, all.size());
  }

  @Test
  public void testGetLocationsByProcessingSiteType() throws Exception {
    List<Location> all = locationRepository.getProcessingSites();
    Assert.assertNotNull("There are processing site Locations", all);
    Assert.assertEquals("There is 1 processing site", 1, all.size());
  }

  @Test
  public void testGetLocationsByDistributionSiteType() throws Exception {
    List<Location> all = locationRepository.getDistributionSites();
    Assert.assertNotNull("There are distribution site Locations", all);
    Assert.assertEquals("There is 1 distribution site", 1, all.size());
  }
}

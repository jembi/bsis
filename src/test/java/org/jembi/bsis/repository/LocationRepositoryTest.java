package org.jembi.bsis.repository;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.user.User;
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
    List<Location> all = locationRepository.getAllLocations(true);
    Assert.assertNotNull("There are Locations", all);
    Assert.assertEquals("There are 11 Locations", 11, all.size());
  }

  @Test
  public void testGetLocation() throws Exception {
    Location one = locationRepository.getLocation(UUID.fromString("55321456-eeee-1234-b5b1-123412348891"));
    Assert.assertNotNull("There is a Location", one);
    Assert.assertEquals("The Location matches", "Maseru", one.getName());
  }

  @Test
  public void testGetLocationDeleted() throws Exception {
    Location one = locationRepository.getLocation(UUID.fromString("55321456-eeee-1234-b5b1-123412348896"));
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
    UUID location1 = UUID.fromString("55321456-eeee-1234-b5b1-123412348891");
    locationRepository.deleteLocation(location1);
    Location one = entityManager.find(Location.class, location1);
    Assert.assertNotNull("There is a Location", one);
    Assert.assertEquals("The Location is marked as deleted", true, one.getIsDeleted());
  }

  @Test
  public void testUpdateLocation() throws Exception {
    String locationName = "Maseru";
    String locationNameUpdated = locationName + "Updated";
    Location one = locationRepository.findLocationByName(locationName);

    Location locationToUpdate = new Location();
    locationToUpdate.setName(locationNameUpdated);
    locationToUpdate.setId(one.getId());
    locationToUpdate.setIsMobileSite(true);
    locationToUpdate.setIsProcessingSite(true);
    locationToUpdate.setIsReferralSite(true);
    locationToUpdate.setIsTestingSite(true);
    locationToUpdate.setIsDistributionSite(true);
    locationToUpdate.setIsUsageSite(true);
    locationToUpdate.setIsVenue(false);

    locationRepository.updateLocation(locationToUpdate);
    Location savedOne = locationRepository.findLocationByName(locationNameUpdated);

    Assert.assertTrue("The location's IsMobilite is updated", savedOne.getIsMobileSite());
    Assert.assertTrue("The location's IsProcessingSite is updated", savedOne.getIsProcessingSite());
    Assert.assertTrue("The location's IsReferralSite is updated", savedOne.getIsReferralSite());
    Assert.assertTrue("The location's IsTestingSite is updated", savedOne.getIsTestingSite());
    Assert.assertTrue("The location's IsDistributionSite is updated", savedOne.getIsDistributionSite());
    Assert.assertTrue("The location's IsUsageSite is updated", savedOne.getIsUsageSite());
    Assert.assertFalse("The location's IsVenue is updated", savedOne.getIsVenue());
    Assert.assertEquals("The location's Name is updated", locationNameUpdated, savedOne.getName());
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
    Assert.assertTrue("Location exists",
        locationRepository.verifyLocationExists(UUID.fromString("55321456-eeee-1234-b5b1-123412348891")));
  }
  
  @Test
  public void testEntityDoesNotExist() throws Exception {
    Assert.assertFalse("Location does not exist",
        locationRepository.verifyLocationExists(UUID.fromString("55321456-eeee-1234-b5b1-123412348999")));
  }

  @Test
  public void testGetUsageSites() throws Exception {
    List<Location> all = locationRepository.getUsageSites();
    Assert.assertNotNull("There are usage site Locations", all);
    Assert.assertEquals("There are 2 usage site Location", 2, all.size());
  }

  @Test
  public void testGetVenues() throws Exception {
    List<Location> all = locationRepository.getVenues();
    Assert.assertNotNull("There are venue Locations", all);
    Assert.assertEquals("There are 3 venue Locations", 4, all.size());
  }

  @Test
  public void testGetProcessingSites() throws Exception {
    List<Location> all = locationRepository.getProcessingSites();
    Assert.assertNotNull("There are processing site Locations", all);
    Assert.assertEquals("There is 1 processing site", 1, all.size());
  }

  @Test
  public void testGetDistributionSites() throws Exception {
    List<Location> all = locationRepository.getDistributionSites();
    Assert.assertNotNull("There are distribution site Locations", all);
    Assert.assertEquals("There is 1 distribution site", 1, all.size());
  }

  @Test
  public void testTestingSites() throws Exception {
    List<Location> all = locationRepository.getTestingSites();
    Assert.assertNotNull("There are testing site Locations", all);
    Assert.assertEquals("There is 1 testing site", 1, all.size());
  }
  
  @Test
  public void testGetReferralSites() throws Exception {
    List<Location> all = locationRepository.getReferralSites();
    Assert.assertNotNull("There are Referral site Locations", all);
    Assert.assertEquals("There is 1 Referral site", 1, all.size());
  }
}

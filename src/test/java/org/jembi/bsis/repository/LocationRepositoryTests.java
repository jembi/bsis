package org.jembi.bsis.repository;

import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aProcessingSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aReferralSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aTestingSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aReferralSite;

import java.util.List;

import org.jembi.bsis.model.location.Location;
import org.jembi.bsis.model.location.LocationType;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class LocationRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private LocationRepository locationRepository;
  
  @Test
  public void testFindLocationByNameSimilarResults_verifyCorrectLocationsReturned() {
    Location location1 = aLocation().withName("testing").buildAndPersist(entityManager); // match
    Location location2 = aLocation().withName("something else").buildAndPersist(entityManager);
    Location location3 = aLocation().withName("retesting").buildAndPersist(entityManager); // match
    Location location4 = aLocation().withName("anything else").buildAndPersist(entityManager);
    Location location5 = aLocation().withName("testingthis").buildAndPersist(entityManager); // match
   
    List<Location> locations = locationRepository.findLocations("test", true, null, true);
    
    // Verify locations returned
    Assert.assertEquals("Verify locations returned", 3, locations.size());

    // Verify right locations were returned
    Assert.assertTrue("Verify locations", locations.contains(location1));
    Assert.assertTrue("Verify locations", locations.contains(location3));
    Assert.assertTrue("Verify locations", locations.contains(location5));
    Assert.assertFalse("Verify locations", locations.contains(location2));
    Assert.assertFalse("Verify locations", locations.contains(location4));
  }
  
  @Test
  public void testFindLocationByNameExact_verifyCorrectLocationReturned() {
    Location location1 = aLocation().withName("testing").buildAndPersist(entityManager); // match
    Location location2 = aLocation().withName("something else").buildAndPersist(entityManager);
    Location location3 = aLocation().withName("retesting").buildAndPersist(entityManager);
    Location location4 = aLocation().withName("anything else").buildAndPersist(entityManager);
    Location location5 = aLocation().withName("testingthis").buildAndPersist(entityManager);
   
    List<Location> locations = locationRepository.findLocations("testing", false, null, true);
    
    // Verify locations returned
    Assert.assertEquals("Verify locations returned", 1, locations.size());

    // Verify right locations were returned
    Assert.assertTrue("Verify locations", locations.contains(location1));
    Assert.assertFalse("Verify locations", locations.contains(location2));
    Assert.assertFalse("Verify locations", locations.contains(location3));
    Assert.assertFalse("Verify locations", locations.contains(location4));
    Assert.assertFalse("Verify locations", locations.contains(location5));
  }
  
  @Test
  public void testFindVenueByNameSimilarResults_verifyCorrectLocationReturned() {
    Location location1 = aVenue().withName("testing").buildAndPersist(entityManager); 
    Location location2 = aVenue().withName("something else").buildAndPersist(entityManager);
    Location location3 = aVenue().withName("retesting").buildAndPersist(entityManager); // match
    Location location4 = aVenue().withName("anything else").buildAndPersist(entityManager);
    Location location5 = aVenue().withName("testingthis").buildAndPersist(entityManager);
   
    List<Location> locations = locationRepository.findLocations("retesting", true, LocationType.VENUE, true);
    
    // Verify locations returned
    Assert.assertEquals("Verify locations returned", 1, locations.size());

    // Verify right locations were returned
    Assert.assertTrue("Verify locations", locations.contains(location3));
    Assert.assertFalse("Verify locations", locations.contains(location1));
    Assert.assertFalse("Verify locations", locations.contains(location2));
    Assert.assertFalse("Verify locations", locations.contains(location4));
    Assert.assertFalse("Verify locations", locations.contains(location5));
  }
  
  @Test
  public void testFindDistributionSites_verifyCorrectLocationsReturned() {
    Location location1 = aProcessingSite().withName("test1").buildAndPersist(entityManager); 
    Location location2 = aDistributionSite().withName("test2").buildAndPersist(entityManager); // match
    Location location3 = aDistributionSite().withName("test3").buildAndPersist(entityManager); // match
    Location location4 = aTestingSite().withName("test4").buildAndPersist(entityManager);
    Location location5 = aUsageSite().withName("test5").buildAndPersist(entityManager);
   
    List<Location> locations = locationRepository.findLocations("", true, LocationType.DISTRIBUTION_SITE, true);
    
    // Verify locations returned
    Assert.assertEquals("Verify locations returned", 2, locations.size());

    // Verify right locations were returned
    Assert.assertTrue("Verify locations", locations.contains(location3));
    Assert.assertTrue("Verify locations", locations.contains(location2));
    Assert.assertFalse("Verify locations", locations.contains(location1));
    Assert.assertFalse("Verify locations", locations.contains(location4));
    Assert.assertFalse("Verify locations", locations.contains(location5));
  }
  
  @Test
  public void testFindProcessingSites_verifyCorrectLocationsReturned() {
    Location location1 = aProcessingSite().withName("test1").buildAndPersist(entityManager); // match
    Location location2 = aDistributionSite().withName("test2").buildAndPersist(entityManager);
    Location location3 = aDistributionSite().withName("test3").buildAndPersist(entityManager); 
    Location location4 = aProcessingSite().withName("test4").buildAndPersist(entityManager); // match
    Location location5 = aUsageSite().withName("test5").buildAndPersist(entityManager);
   
    List<Location> locations = locationRepository.findLocations(null, false, LocationType.PROCESSING_SITE, true);
    
    // Verify locations returned
    Assert.assertEquals("Verify locations returned", 2, locations.size());

    // Verify right locations were returned
    Assert.assertTrue("Verify locations", locations.contains(location1));
    Assert.assertTrue("Verify locations", locations.contains(location4));
    Assert.assertFalse("Verify locations", locations.contains(location2));
    Assert.assertFalse("Verify locations", locations.contains(location3));
    Assert.assertFalse("Verify locations", locations.contains(location5));
  }
  
  @Test
  public void testFindTestingSites_verifyCorrectLocationsReturned() {
    Location location1 = aTestingSite().withName("test1").buildAndPersist(entityManager); // match
    Location location2 = aDistributionSite().withName("test2").buildAndPersist(entityManager);
    Location location3 = aProcessingSite().withName("test3").buildAndPersist(entityManager); 
    Location location4 = aTestingSite().withName("test4").buildAndPersist(entityManager); // match
    Location location5 = aUsageSite().withName("test5").buildAndPersist(entityManager);
   
    List<Location> locations = locationRepository.findLocations(null, true, LocationType.TESTING_SITE, true);
    
    // Verify locations returned
    Assert.assertEquals("Verify locations returned", 2, locations.size());

    // Verify right locations were returned
    Assert.assertTrue("Verify locations", locations.contains(location1));
    Assert.assertTrue("Verify locations", locations.contains(location4));
    Assert.assertFalse("Verify locations", locations.contains(location2));
    Assert.assertFalse("Verify locations", locations.contains(location3));
    Assert.assertFalse("Verify locations", locations.contains(location5));
  }

  @Test
  public void testFindReferralSites_verifyCorrectLocationsReturned() {
    Location location1 = aReferralSite().withName("test1").buildAndPersist(entityManager); // match
    Location location2 = aDistributionSite().withName("test2").buildAndPersist(entityManager);
    Location location3 = aProcessingSite().withName("test3").buildAndPersist(entityManager); 
    Location location4 = aReferralSite().withName("test4").buildAndPersist(entityManager); // match
    Location location5 = aUsageSite().withName("test5").buildAndPersist(entityManager);

    List<Location> locations = locationRepository.findLocations(null, true, LocationType.REFERRAL_SITE, true);

    // Verify locations returned
    Assert.assertEquals("Verify locations returned", 2, locations.size());

    // Verify right locations were returned
    Assert.assertTrue("Verify locations", locations.contains(location1));
    Assert.assertTrue("Verify locations", locations.contains(location4));
    Assert.assertFalse("Verify locations", locations.contains(location2));
    Assert.assertFalse("Verify locations", locations.contains(location3));
    Assert.assertFalse("Verify locations", locations.contains(location5));
  }

  @Test
  public void testGetReferralSites_verifyCorrectLocationsReturned() {
    Location location1 = aReferralSite().withName("test1").buildAndPersist(entityManager); // match
    Location location2 = aDistributionSite().withName("test2").buildAndPersist(entityManager);
    Location location3 = aProcessingSite().withName("test3").buildAndPersist(entityManager); 
    Location location4 = aReferralSite().withName("test4").buildAndPersist(entityManager); // match
    Location location5 = aUsageSite().withName("test5").buildAndPersist(entityManager);
   
    List<Location> locations = locationRepository.getReferralSites();
    
    // Verify locations returned
    Assert.assertEquals("Verify locations returned", 2, locations.size());

    // Verify right locations were returned
    Assert.assertTrue("Verify locations", locations.contains(location1));
    Assert.assertTrue("Verify locations", locations.contains(location4));
    Assert.assertFalse("Verify locations", locations.contains(location2));
    Assert.assertFalse("Verify locations", locations.contains(location3));
    Assert.assertFalse("Verify locations", locations.contains(location5));
  }

  @Test
  public void testFindUsageSites_verifyCorrectLocationsReturned() {
    Location location1 = aTestingSite().withName("test1").buildAndPersist(entityManager);
    Location location2 = aDistributionSite().withName("test2").buildAndPersist(entityManager);
    Location location3 = aProcessingSite().withName("test3").buildAndPersist(entityManager); 
    Location location4 = aTestingSite().withName("test4").buildAndPersist(entityManager);
    Location location5 = aUsageSite().withName("test5").buildAndPersist(entityManager); // match
   
    List<Location> locations = locationRepository.findLocations(null, false, LocationType.USAGE_SITE, true);
    
    // Verify locations returned
    Assert.assertEquals("Verify locations returned", 1, locations.size());

    // Verify right locations were returned
    Assert.assertTrue("Verify locations", locations.contains(location5));
    Assert.assertFalse("Verify locations", locations.contains(location1));
    Assert.assertFalse("Verify locations", locations.contains(location3));
    Assert.assertFalse("Verify locations", locations.contains(location4));
  }
  
  @Test
  public void testFindAllSites_verifyNonDeletedLocationsReturned() {
    Location location1 = aTestingSite().withName("test1").buildAndPersist(entityManager);
    Location location2 = aDistributionSite().withName("test2").buildAndPersist(entityManager);
    Location location3 = aProcessingSite().withName("test3").thatIsDeleted().buildAndPersist(entityManager);  // no match 
    Location location4 = aTestingSite().withName("test4").buildAndPersist(entityManager);
    Location location5 = aUsageSite().withName("test5").buildAndPersist(entityManager);
   
    List<Location> locations = locationRepository.findLocations(null, false, null, false);
    
    // Verify locations returned
    Assert.assertEquals("Verify locations returned", 4, locations.size());

    // Verify right locations were returned
    Assert.assertTrue("Verify locations", locations.contains(location1));
    Assert.assertTrue("Verify locations", locations.contains(location2));
    Assert.assertFalse("Verify locations", locations.contains(location3));
    Assert.assertTrue("Verify locations", locations.contains(location4));
    Assert.assertTrue("Verify locations", locations.contains(location5));
  }
  
  @Test
  public void testFindVenueNoMatches_verifyNoLocationsReturned() {
    Location location1 = aTestingSite().withName("test1").buildAndPersist(entityManager);
    Location location2 = aDistributionSite().withName("test2").buildAndPersist(entityManager);
    Location location3 = aProcessingSite().withName("test3").buildAndPersist(entityManager); 
    Location location4 = aTestingSite().withName("test4").buildAndPersist(entityManager);
    Location location5 = aUsageSite().withName("test5").buildAndPersist(entityManager);
   
    List<Location> locations = locationRepository.findLocations("test", false, LocationType.VENUE, true);
    
    // Verify locations returned
    Assert.assertEquals("Verify locations returned", 0, locations.size());

    // Verify right locations were returned
    Assert.assertFalse("Verify locations", locations.contains(location1));
    Assert.assertFalse("Verify locations", locations.contains(location4));
    Assert.assertFalse("Verify locations", locations.contains(location2));
    Assert.assertFalse("Verify locations", locations.contains(location3));
    Assert.assertFalse("Verify locations", locations.contains(location5));
  }
  
  @Test
  public void testGetMobileVenues_verifyCorrectLocationsReturned() {
    Location location1 = aVenue().withName("test1").thatIsMobileSite().buildAndPersist(entityManager); // match
    Location location2 = aDistributionSite().withName("test2").buildAndPersist(entityManager);
    Location location3 = aProcessingSite().withName("test3").buildAndPersist(entityManager);
    Location location4 = aVenue().withName("test4").buildAndPersist(entityManager); 
    Location location5 = aVenue().withName("test5").thatIsMobileSite().buildAndPersist(entityManager); // match
    Location location6 = aTestingSite().withName("test6").buildAndPersist(entityManager);
   
    List<Location> locations = locationRepository.getMobileVenues();
    
    // Verify locations returned
    Assert.assertEquals("Verify locations returned", 2, locations.size());

    // Verify right locations were returned
    Assert.assertTrue("Verify locations", locations.contains(location1));
    Assert.assertFalse("Verify locations", locations.contains(location4));
    Assert.assertFalse("Verify locations", locations.contains(location2));
    Assert.assertFalse("Verify locations", locations.contains(location3));
    Assert.assertTrue("Verify locations", locations.contains(location5));
    Assert.assertFalse("Verify locations", locations.contains(location6));
  }
}

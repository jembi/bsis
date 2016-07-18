package org.jembi.bsis.model.location;

import static org.jembi.bsis.helpers.builders.LocationBuilder.aDistributionSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aLocation;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aProcessingSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aTestingSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aUsageSite;
import static org.jembi.bsis.helpers.builders.LocationBuilder.aVenue;

import org.junit.Assert;
import org.junit.Test;

public class LocationTypeTest {

  @Test
  public void testLocationTypeVenue_returnsCorrectEnum() throws Exception {
    Location location = aVenue().build();
    Assert.assertEquals("Location Type is VENUE", LocationType.VENUE, LocationType.resolveLocationType(location));
  }
  
  @Test
  public void testLocationTypeDistributionSite_returnsCorrectEnum() throws Exception {
    Location location = aDistributionSite().build();
    Assert.assertEquals("Location Type is DISTRIBUTION_SITE", LocationType.DISTRIBUTION_SITE, LocationType.resolveLocationType(location));
  }
  
  @Test
  public void testLocationTypeTestingSite_returnsCorrectEnum() throws Exception {
    Location location = aTestingSite().build();
    Assert.assertEquals("Location Type is TESTING_SITE", LocationType.TESTING_SITE, LocationType.resolveLocationType(location));
  }
  
  @Test
  public void testLocationTypeProcessingSite_returnsCorrectEnum() throws Exception {
    Location location = aProcessingSite().build();
    Assert.assertEquals("Location Type is PROCESSING_SITE", LocationType.PROCESSING_SITE, LocationType.resolveLocationType(location));
  }
  
  @Test
  public void testLocationTypeUsageSite_returnsCorrectEnum() throws Exception {
    Location location = aUsageSite().build();
    Assert.assertEquals("Location Type is USAGE_SITE", LocationType.USAGE_SITE, LocationType.resolveLocationType(location));
  }
  
  @Test(expected = java.lang.IllegalArgumentException.class)
  public void testLocationTypeNoneSpecified_throws() throws Exception {
    Location location = aLocation().build();
    LocationType.resolveLocationType(location);
  }
}

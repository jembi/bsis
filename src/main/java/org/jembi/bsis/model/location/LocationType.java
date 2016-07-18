package org.jembi.bsis.model.location;

public enum LocationType {

  VENUE, PROCESSING_SITE, DISTRIBUTION_SITE, USAGE_SITE, TESTING_SITE;
  
  public static LocationType resolveLocationType(Location location) {
    if (location.getIsVenue()) {
      return VENUE;
    }
    if (location.getIsProcessingSite()) {
      return PROCESSING_SITE;
    }
    if (location.getIsDistributionSite()) {
      return DISTRIBUTION_SITE;
    }
    if (location.getIsUsageSite()) {
      return USAGE_SITE;
    }
    if (location.getIsTestingSite()) {
      return TESTING_SITE;
    }
    else throw new IllegalArgumentException("Unknown LocationType for location " + location);
  }
}

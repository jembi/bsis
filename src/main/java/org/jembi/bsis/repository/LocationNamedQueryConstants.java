package org.jembi.bsis.repository;

public class LocationNamedQueryConstants {

  public static final String NAME_GET_ALL_LOCATIONS = "Location.getAllLocations";
  public static final String QUERY_GET_ALL_LOCATIONS = "SELECT l FROM Location l "
      + "WHERE :includeDeleted = TRUE OR l.isDeleted = FALSE "
      + "ORDER BY name ASC";
  
  public static final String NAME_COUNT_LOCATION_WITH_ID = "Location.countLocationWithId";
  public static final String QUERY_COUNT_LOCATION_WITH_ID =
      "SELECT count(*) FROM Location l WHERE l.id=:id AND l.isDeleted = false";
  
  public static final String NAME_FIND_VENUES = "Location.findVenues";
  public static final String QUERY_FIND_VENUES =
      "SELECT l "
      + "FROM Location l "
      + "WHERE l.isVenue = :isVenue "
      + "AND l.isDeleted = :isDeleted "
      + "ORDER BY l.name ASC";
  
  public static final String NAME_FIND_MOBILE_VENUES = "Location.findMobileVenues";
  public static final String QUERY_FIND_MOBILE_VENUES =
      "SELECT l "
      + "FROM Location l "
      + "WHERE l.isVenue = :isVenue "
      + "AND l.isDeleted = :isDeleted "
      + "AND l.isMobileSite = :isMobileSite "
      + "ORDER BY l.name ASC";
  
  public static final String NAME_FIND_PROCESSING_SITES = "Location.findProcessingSites";
  public static final String QUERY_FIND_PROCESSING_SITES =
      "SELECT l "
      + "FROM Location l "
      + "WHERE l.isProcessingSite = :isProcessingSite "
      + "AND l.isDeleted = :isDeleted "
      + "ORDER BY l.name ASC";
  
  public static final String NAME_FIND_USAGE_SITES = "Location.findUsageSites";
  public static final String QUERY_FIND_USAGE_SITES =
      "SELECT l "
      + "FROM Location l "
      + "WHERE l.isUsageSite = :isUsageSite "
      + "AND l.isDeleted = :isDeleted "
      + "ORDER BY l.name ASC";

  public static final String NAME_FIND_DISTRIBUTION_SITES = "Location.findDistributionSites";
  public static final String QUERY_FIND_DISTRIBUTION_SITES =
      "SELECT l "
      + "FROM Location l "
      + "WHERE l.isDistributionSite = :isDistributionSite "
      + "AND l.isDeleted = :isDeleted "
      + "ORDER BY l.name ASC";
  
  public static final String NAME_FIND_TESTING_SITES = "Location.findTestingSites";
  public static final String QUERY_FIND_TESTING_SITES =
      "SELECT l "
      + "FROM Location l "
      + "WHERE l.isTestingSite = :isTestingSite "
      + "AND l.isDeleted = :isDeleted "
      + "ORDER BY l.name ASC";
  
  public static final String NAME_FIND_REFERRAL_SITES = "Location.findReferralSites";
  public static final String QUERY_FIND_REFERRAL_SITES =
      "SELECT l "
      + "FROM Location l "
      + "WHERE l.isReferralSite = :isReferralSite "
      + "AND l.isDeleted = :isDeleted "
      + "ORDER BY l.name ASC";
}

package repository;

public class LocationNamedQueryConstants {

  public static final String NAME_GET_ALL_LOCATIONS = "Location.getAllLocations";
  public static final String QUERY_GET_ALL_LOCATIONS = "SELECT l FROM Location l ORDER BY name ASC";
  
  public static final String NAME_GET_ALL_USAGE_SITES = "Location.getAllUsageSites";
  public static final String QUERY_GET_ALL_USAGE_SITES =
      "SELECT l from Location l where l.isUsageSite=:isUsageSite and l.isDeleted=:isDeleted ORDER BY name ASC";

  public static final String NAME_GET_ALL_VENUES = "Location.getAllVenues";
  public static final String QUERY_GET_ALL_VENUES =
      "SELECT l from Location l where l.isVenue=:isVenue and l.isDeleted=:isDeleted ORDER BY name ASC";
  
  public static final String NAME_COUNT_LOCATION_WITH_ID = "Location.countLocationWithId";
  public static final String QUERY_COUNT_LOCATION_WITH_ID =
      "SELECT count(*) FROM Location l WHERE l.id=:id AND l.isDeleted = false";

  public static final String NAME_GET_LOCATIONS_BY_TYPE = "Location.getLocationsByType";
  public static final String QUERY_GET_LOCATIONS_BY_TYPE =
      "SELECT l from Location l where l.isProcessingSite=:isProcessingSite and l.isVenue=:isVenue and l.isUsageSite=:isUsageSite and l.isDeleted=:isDeleted ORDER BY name ASC";
}

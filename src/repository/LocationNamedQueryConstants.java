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
}

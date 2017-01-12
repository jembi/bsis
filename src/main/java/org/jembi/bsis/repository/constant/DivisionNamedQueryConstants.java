package org.jembi.bsis.repository.constant;

public class DivisionNamedQueryConstants {
  
  public static final String NAME_FIND_DIVISION_BY_ID =
      "Division.findDivisionById";
  public static final String QUERY_FIND_DIVISION_BY_ID =
      "SELECT d "
      + "FROM Division d "
      + "WHERE d.id = :id ";
  
  public static final String NAME_FIND_DIVISION_BY_NAME =
      "Division.findDivisionByName";
  public static final String QUERY_FIND_DIVISION_BY_NAME =
      "SELECT d "
      + "FROM Division d "
      + "WHERE d.name = :name ";
  
  public static final String NAME_COUNT_DIVISIONS_WITH_PARENT =
      "Division.countDivisionsWithParent";
  public static final String QUERY_COUNT_DIVISIONS_WITH_PARENT =
      "SELECT COUNT(d) "
      + "FROM Division d "
      + "WHERE d.parent = :parent ";

  public static final String NAME_GET_ALL_DIVISIONS = 
      "Division.getAllDivisions";
  public static final String QUERY_GET_ALL_DIVISIONS = 
      "SELECT d FROM Division d ORDER BY name ASC";
}

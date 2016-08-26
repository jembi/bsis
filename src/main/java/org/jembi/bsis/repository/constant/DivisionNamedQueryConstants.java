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
  
  public static final String NAME_COUNT_DIVISIONS_BY_PARENT =
      "Division.countDivisionsByParent";
  public static final String QUERY_COUNT_DIVISIONS_BY_PARENT =
      "SELECT COUNT(d) "
      + "FROM Division d "
      + "WHERE d.parent = :parent ";

}

package org.jembi.bsis.repository.constant;

public class DivisionNamedQueryConstants {
  
  public static final String NAME_FIND_DIVISION_BY_ID =
      "Division.findDivisionById";
  public static final String QUERY_FIND_DIVISION_BY_ID =
      "SELECT d "
      + "FROM Division d "
      + "WHERE d.id = :id ";
  
  public static final String NAME_COUNT_DIVISION_WITH_ID = 
      "Division.countDivisionWithId";
  public static final String QUERY_COUNT_DIVISION_WITH_ID = 
      "SELECT count(*) "
      + "FROM Division d "
      + "WHERE d.id=:id";
  
  public static final String NAME_FIND_DIVISION_BY_NAME =
      "Division.findDivisionByName";
  public static final String QUERY_FIND_DIVISION_BY_NAME =
      "SELECT d "
      + "FROM Division d "
      + "WHERE d.name = :name ";

}

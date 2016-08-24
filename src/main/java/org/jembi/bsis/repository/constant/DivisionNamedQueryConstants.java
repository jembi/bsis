package org.jembi.bsis.repository.constant;

public class DivisionNamedQueryConstants {
  
  public static final String NAME_FIND_DIVISION_BY_ID =
      "Division.findDivisionById";
  public static final String QUERY_FIND_DIVISION_BY_ID =
      "SELECT d "
      + "FROM Division d "
      + "WHERE d.id = :id ";

}

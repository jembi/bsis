package org.jembi.bsis.repository;

public class ReturnFormNamedQueryConstants {

  public static final String NAME_FIND_BY_ID = "ReturnForm.findById";
  public static final String QUERY_FIND_BY_ID =
      "SELECT r FROM ReturnForm r WHERE r.id = :id AND r.isDeleted = :isDeleted";

}

package org.jembi.bsis.repository;

public class ComponentTypeQueryConstants {
 
  public static final String NAME_VERIFY_COMPONENT_TYPE_WITH_ID_EXISTS =
      "ComponentType.verifyExists";
  public static final String QUERY_VERIFY_COMPONENT_TYPE_WITH_ID_EXISTS =
      "SELECT count(ct) > 0 " +
          "FROM ComponentType ct " +
          "WHERE ct.id = :id " +
          "AND ct.isDeleted = :deleted ";

}
package org.jembi.bsis.repository;

public class ComponentTypeQueryConstants {
 
  public static final String NAME_VERIFY_COMPONENT_TYPE_WITH_ID_EXISTS =
      "ComponentType.verifyExists";
  public static final String QUERY_VERIFY_COMPONENT_TYPE_WITH_ID_EXISTS =
      "SELECT count(ct) > 0 " +
          "FROM ComponentType ct " +
          "WHERE ct.id = :id " +
          "AND ct.isDeleted = :deleted ";
  
  public static final String NAME_FIND_COMPONENT_TYPE_BY_CODE =
      "ComponentType.findComponentTypeByCode";
  public static final String QUERY_FIND_COMPONENT_TYPE_BY_CODE =
      "SELECT ct "
      + "FROM ComponentType ct "
      + "WHERE ct.componentTypeCode = :componentTypeCode ";

}
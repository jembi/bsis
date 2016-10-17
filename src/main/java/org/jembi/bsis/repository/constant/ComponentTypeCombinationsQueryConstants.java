package org.jembi.bsis.repository.constant;

public class ComponentTypeCombinationsQueryConstants {

  public static final String NAME_FIND_COMPONENT_TYPE_COMBINATION =
      "ComponentTypeCombination.findComponentTypeCombination";
  public static final String QUERY_FIND_COMPONENT_TYPE_COMBINATION =
      "select c "
      +"from ComponentTypeCombination c "
      +"where c.isDeleted = :includeDeleted) ";  
}
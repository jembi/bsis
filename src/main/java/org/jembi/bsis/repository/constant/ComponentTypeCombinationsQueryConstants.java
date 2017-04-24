package org.jembi.bsis.repository.constant;

public class ComponentTypeCombinationsQueryConstants {

  public static final String NAME_FIND_COMPONENT_TYPE_COMBINATION =
      "ComponentTypeCombination.findComponentTypeCombination";
  public static final String QUERY_FIND_COMPONENT_TYPE_COMBINATION =
      "SELECT c " + 
          "FROM ComponentTypeCombination c " +
          "WHERE  (:includeDeleted = TRUE OR c.isDeleted = FALSE)";
  
  public static final String NAME_VERIFY_UNIQUE_COMPONENT_TYPE_COMBINATION_NAME =
      "ComponentTypeCombination.isUniqueCombinationName";
  public static final String QUERY_VERIFY_UNIQUE_COMPONENT_TYPE_COMBINATION_NAME =
      "SELECT count(c) = 0 " +
          "FROM ComponentTypeCombination c " +
          "WHERE c.combinationName = :combinationName " +
          "AND (:idIncluded is true OR c.id != :id) ";

  public static final String NAME_VERIFY_COMPONENT_TYPE_COMBINATION_WITH_ID_EXISTS =
      "ComponentTypeCombination.verifyExists";
  public static final String QUERY_VERIFY_COMPONENT_TYPE_COMBINATION_WITH_ID_EXISTS =
      "SELECT count(ctc) > 0 " +
          "FROM ComponentTypeCombination ctc " +
          "WHERE ctc.id = :id " +
          "AND ctc.isDeleted = :deleted ";
}

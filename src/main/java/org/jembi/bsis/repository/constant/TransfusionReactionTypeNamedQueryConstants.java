package org.jembi.bsis.repository.constant;

public class TransfusionReactionTypeNamedQueryConstants {
  public static final String NAME_GET_ALL_TRANSFUSION_REACTION_TYPES =
      "TransfusionReactionType.getAllTransfusionReactionTypes";
  public static final String QUERY_GET_ALL_TRANSFUSION_REACTION_TYPES =
      "SELECT t FROM TransfusionReactionType t "
      + "WHERE (:includeDeleted = TRUE OR t.isDeleted = FALSE)"
      + "ORDER BY name ASC ";

  public static final String NAME_FIND_BY_ID = "TransfusionReactionType.findById";
  public static final String QUERY_FIND_BY_ID =
      "SELECT trt FROM TransfusionReactionType trt WHERE trt.id = :id";
  
  public static final String NAME_VERIFY_UNIQUE_TRANSFUSION_REACTION_TYPE_NAME =
      "TransfusionReactionType.isUniqueTransfusionReactionTypeName";
  public static final String QUERY_VERIFY_UNIQUE_TRANSFUSION_REACTION_TYPE_NAME =
      "SELECT count(t) = 0 " +
          "FROM TransfusionReactionType t " +
          "WHERE t.name = :reactionTypeName " +
          "AND (:includeId = false OR t.id != :id) ";
}

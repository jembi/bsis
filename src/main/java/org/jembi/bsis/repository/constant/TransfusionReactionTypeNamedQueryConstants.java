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
      "SELECT trt FROM TransfusionReactionType trt WHERE trt.id = :id AND trt.isDeleted = :isDeleted";
}

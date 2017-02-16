package org.jembi.bsis.repository.constant;

public class TransfusionReactionTypeNamedQueryConstants {
  public static final String NAME_GET_ALL_TRANSFUSION_REACTION_TYPES =
      "TransfusionReactionType.getAllTransfusionReactionTypes";
  public static final String QUERY_GET_ALL_TRANSFUSION_REACTION_TYPES =
      "SELECT t FROM TransfusionReactionType t "
      + "WHERE (:includeDeleted = TRUE OR t.isDeleted = FALSE)"
      + "ORDER BY name ASC ";
}

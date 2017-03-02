package org.jembi.bsis.repository.constant;

public class TransfusionNamedQueryConstants {

  public static final String NAME_FIND_TRANSFUSIONS_RECORDED =
      "Transfusion.findTransfusionsRecorded";
  public static final String QUERY_FIND_TRANSFUSIONS_RECORDED =
      "SELECT NEW org.jembi.bsis.dto.TransfusionSummaryDTO(t.transfusionOutcome, t.transfusionReactionType, COUNT(t), t.transfusionSite) " +
      "FROM Transfusion t " +
      "WHERE :receivedFrom = NULL OR t.receivedFrom = :receivedFrom " +
      "AND t.dateTransfused BETWEEN :startDate AND :endDate " +
      "AND t.isDeleted = :transfusionDeleted " +
      "GROUP BY t.transfusionOutcome, t.transfusionReactionType, t.receivedFrom " +
      "ORDER BY t.dateTransfused DESC";
}
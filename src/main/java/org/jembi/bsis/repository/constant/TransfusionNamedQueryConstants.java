package org.jembi.bsis.repository.constant;

public class TransfusionNamedQueryConstants {

  public static final String NAME_FIND_TRANSFUSION_SUMMARY_RECORDED_FOR_USAGE_SITE_FOR_PERIOD =
      "Transfusion.findTransfusionSummaryRecordedForUsageSiteForPeriod";
  public static final String QUERY_FIND_TRANSFUSION_SUMMARY_RECORDED_FOR_USAGE_SITE_FOR_PERIOD =
      "SELECT NEW org.jembi.bsis.dto.TransfusionSummaryDTO(t.transfusionOutcome, t.transfusionReactionType, t.receivedFrom, COUNT(t)) " +
      "FROM Transfusion t " +
      "WHERE :receivedFromId = NULL OR t.receivedFrom.id = :receivedFromId " +
      "AND t.dateTransfused BETWEEN :startDate AND :endDate " +
      "AND t.isDeleted = :transfusionDeleted " +
      "GROUP BY t.receivedFrom, t.transfusionOutcome, t.transfusionReactionType " +
      "ORDER BY t.receivedFrom, t.transfusionOutcome";
}
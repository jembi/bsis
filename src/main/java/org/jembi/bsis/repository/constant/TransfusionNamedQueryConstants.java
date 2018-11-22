package org.jembi.bsis.repository.constant;

public class TransfusionNamedQueryConstants {

  public static final String NAME_FIND_TRANSFUSION_SUMMARY_RECORDED_FOR_USAGE_SITE_FOR_PERIOD =
      "Transfusion.findTransfusionSummaryRecordedForUsageSiteForPeriod";
  public static final String QUERY_FIND_TRANSFUSION_SUMMARY_RECORDED_FOR_USAGE_SITE_FOR_PERIOD =
      "SELECT NEW org.jembi.bsis.dto.TransfusionSummaryDTO(t.transfusionOutcome, t.transfusionReactionType, t.receivedFrom, COUNT(t)) " +
      "FROM Transfusion t " +
      "LEFT JOIN t.transfusionReactionType " +
      "WHERE (:includeAllLocations is true OR t.receivedFrom.id = :receivedFromId) " +
      "AND (t.dateTransfused BETWEEN :startDate AND :endDate) " +
      "AND (t.isDeleted = :transfusionDeleted) " +
      "GROUP BY t.receivedFrom, t.transfusionOutcome, t.transfusionReactionType " +
      "ORDER BY t.receivedFrom, t.transfusionOutcome";

  public static final String NAME_FIND_TRANSFUSION_BY_DIN_AND_COMPONENT_CODE =
      "Transfusion.findTransfusionByDINAndComponentCode";
  public static final String QUERY_FIND_TRANSFUSION_BY_DIN_AND_COMPONENT_CODE =
      "SELECT t from Transfusion t "
      + "LEFT JOIN t.component.donation AS d "
      + "WHERE (d.donationIdentificationNumber = :donationIdentificationNumber "
      + "OR CONCAT(d.donationIdentificationNumber, d.flagCharacters) = :donationIdentificationNumber) "
      + "AND t.component.componentCode = :componentCode AND t.isDeleted = :isDeleted";

  public static final String NAME_FIND_TRANSFUSIONS =
      "Transfusion.findTransfusions";
  public static final String QUERY_FIND_TRANSFUSIONS =
      "SELECT t from Transfusion t " +
      "WHERE t.isDeleted = :isDeleted " +
      "AND (:includeAllComponentTypes is true OR t.component.componentType.id = :componentTypeId) " +
      "AND (:includeTransfusionOutcome is false OR t.transfusionOutcome = :transfusionOutcome) " +
      "AND (:includeAllLocations is true OR t.receivedFrom.id = :receivedFromId) " +
      "AND (:startDate is null OR t.dateTransfused >= :startDate) " +
      "AND (:endDate is null OR t.dateTransfused <= :endDate) ";

  public static final String NAME_FIND_TRANSFUSION_BY_ID =
      "Transfusion.findTransfusionById";
  public static final String QUERY_FIND_TRANSFUSION_BY_ID =
      "SELECT t FROM Transfusion t WHERE t.id = :transfusionId AND t.isDeleted = :isDeleted";
}
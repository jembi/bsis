package org.jembi.bsis.repository.constant;

public class TranfusionNamedQueryConstants {
  
  public static final String NAME_FIND_TRANSFUSION_BY_DIN_AND_COMPONENT_CODE =
      "Transfusion.findTransfusionByDINAndCode";
  public static final String QUERY_FIND_TRANSFUSION_BY_DIN_AND_COMPONENT_CODE =
      "SELECT t from Transfusion t "
      + "LEFT JOIN t.component.donation AS d "
      + "WHERE (d.donationIdentificationNumber = :donationIdentificationNumber "
      + "OR CONCAT(d.donationIdentificationNumber, d.flagCharacters) = :donationIdentificationNumber) "
      + "AND t.component.componentCode = :componentCode AND t.isDeleted = :isDeleted";

  public static final String NAME_FIND_TRANSFUSION_BY_COMPONENT_TYPE_AND_SITE_AND_OUTCOME =
      "Transfusion.findTransfusionByComponentTypeAndSiteAndOutcome";
  public static final String QUERY_FIND_TRANSFUSION_BY_COMPONENT_TYPE_AND_SITE_AND_OUTCOME =
      "SELECT t from Transfusion t " +
      "WHERE t.isDeleted = :isDeleted " +
      "AND (:componentTypeId is null OR t.component.componentType.id = :componentTypeId) " +
      "AND (:includeTransfusionOutcome is false OR t.transfusionOutcome = :transfusionOutcome) " +
      "AND (:receivedFromId is null OR t.receivedFrom.id = :receivedFromId) " +
      "AND (:startDate is null OR t.dateTransfused >= :startDate) " +
      "AND (:endDate is null OR t.dateTransfused <= :endDate) ";
}

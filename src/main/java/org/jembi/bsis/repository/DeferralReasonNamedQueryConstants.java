package org.jembi.bsis.repository;

public class DeferralReasonNamedQueryConstants {
  
  public static final String NAME_FIND_ALL_DEFERRAL_REASONS =
      "DeferralReason.findAllDeferralReasons";
  public static final String QUERY_FIND_ALL_DEFERRAL_REASONS =
      "SELECT dr " +
          "FROM DeferralReason dr " +
          "WHERE dr.isDeleted = :deleted ";

  public static final String NAME_FIND_DEFERRAL_REASON_BY_TYPE =
      "DeferralReason.findDeferralReasonByType";
  public static final String QUERY_FIND_DEFERRAL_REASON_BY_TYPE =
      "SELECT dr " +
          "FROM DeferralReason dr " +
          "WHERE dr.type = :type " +
          "AND dr.isDeleted = :deleted ";
  
  public static final String NAME_COUNT_DEFERRAL_REASONS_FOR_ID =
      "DeferralReason.countDeferralReasonsForId";
  public static final String QUERY_COUNT_DEFERRAL_REASONS_FOR_ID =
      "SELECT COUNT(dr) "
      + "FROM DeferralReason dr "
      + "WHERE dr.id = :id "
      + "AND dr.isDeleted = :deleted ";

}

package repository;

public class DeferralReasonNamedQueryConstants {

  public static final String NAME_FIND_DEFERRAL_REASON_BY_TYPE =
      "DeferralReason.findDeferralReasonByType";
  public static final String QUERY_FIND_DEFERRAL_REASON_BY_TYPE =
      "SELECT dr " +
          "FROM DeferralReason dr " +
          "WHERE dr.type = :type " +
          "AND dr.isDeleted = :deleted ";

}

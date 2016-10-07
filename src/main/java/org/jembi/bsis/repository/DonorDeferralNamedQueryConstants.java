package org.jembi.bsis.repository;

public class DonorDeferralNamedQueryConstants {

  public static final String NAME_COUNT_DONOR_DEFERRALS_FOR_DONOR =
      "DonorDeferral.countDonorDeferralsForDonor";
  public static final String QUERY_COUNT_DONOR_DEFERRALS_FOR_DONOR =
      "SELECT COUNT(dd) " +
          "FROM DonorDeferral dd " +
          "WHERE dd.deferredDonor = :donor " +
          "AND dd.isVoided = :voided ";

  public static final String NAME_COUNT_CURRENT_DONOR_DEFERRALS_FOR_DONOR =
      "DonorDeferral.countCurrentDonorDeferralsForDonor";
  public static final String QUERY_COUNT_CURRENT_DONOR_DEFERRALS_FOR_DONOR =
      "SELECT COUNT(dd) " +
          "FROM DonorDeferral dd " +
          "WHERE dd.deferredDonor.id = :donorId " +
          "AND dd.isVoided = :voided " +
          "AND (dd.deferralReason.durationType = :permanentDuration " +
          " OR dd.deferredUntil > :currentDate) ";

  public static final String NAME_FIND_DONOR_DEFERRALS_FOR_DONOR_BY_DEFERRAL_REASON =
      "DonorDeferral.findDonorDeferralsForDonorByDeferralReason";
  public static final String QUERY_FIND_DONOR_DEFERRALS_FOR_DONOR_BY_DEFERRAL_REASON =
      "SELECT dd " +
          "FROM DonorDeferral dd " +
          "WHERE dd.deferredDonor = :donor " +
          "AND dd.deferralReason = :deferralReason " +
          "AND dd.isVoided = :voided ";
  
  public static final String NAME_COUNT_DEFERRALS_BY_VENUE_DEFERRAL_REASON_AND_GENDER =
      "DonorDeferral.countDeferralsByVenueDeferralReasonAndGender";
  public static final String QUERY_COUNT_DEFERRALS_BY_VENUE_DEFERRAL_REASON_AND_GENDER =
      "SELECT NEW org.jembi.bsis.dto.DeferredDonorsDTO(dd.deferralReason, dd.deferredDonor.gender, dd.deferredDonor.venue, COUNT(*)) " +
          "FROM DonorDeferral dd " +
          "WHERE dd.isVoided = :deferralDeleted AND dd.deferralReason.isDeleted = :deferralReasonDeleted " + 
          "AND dd.deferredDonor.isDeleted = :donorDeleted " +
          "AND dd.deferralDate BETWEEN :startDate AND :endDate " +
          "GROUP BY dd.deferredDonor.venue, dd.deferralReason, dd.deferredDonor.gender " +
          "ORDER BY dd.deferredDonor.venue, dd.deferredDonor.gender";

  public static final String NAME_FIND_DONOR_DEFERRAL_BY_ID =
      "DonorDeferral.findDonorDeferralsById";
  public static final String QUERY_FIND_DONOR_DEFERRAL_BY_ID =
      "SELECT dd FROM DonorDeferral dd " +
          "LEFT JOIN FETCH dd.deferredDonor " +
          "WHERE dd.id = :donorDeferralId " +
          "AND dd.isVoided = :voided";
  
  public static final String NAME_FIND_DEFERRALS_FOR_EXPORT =
      "DonorDeferral.findDeferralsForExport";
  public static final String QUERY_FIND_DEFERRALS_FOR_EXPORT =
      "SELECT NEW org.jembi.bsis.dto.DeferralExportDTO(d.deferredDonor.donorNumber, d.modificationTracker.createdDate, "
      + "d.modificationTracker.createdBy.username, d.modificationTracker.lastUpdated, "
      + "d.modificationTracker.lastUpdatedBy.username, d.deferralReasonText, d.deferralReason.reason, d.deferralDate, d.deferredUntil) "
      + "FROM DonorDeferral d "
      + "WHERE d.isVoided = :voided "
      + "ORDER BY d.modificationTracker.createdDate ASC "; 
}

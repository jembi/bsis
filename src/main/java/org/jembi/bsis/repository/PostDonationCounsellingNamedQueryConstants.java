package org.jembi.bsis.repository;

public class PostDonationCounsellingNamedQueryConstants {

  public static final String NAME_FIND_POST_DONATION_COUNSELLING_FOR_DONOR =
      "PostDonationCounselling.findPostDonationCounsellingForDonor";
  public static final String QUERY_FIND_POST_DONATION_COUNSELLING_FOR_DONOR =
      "SELECT pdc " +
          "FROM PostDonationCounselling pdc " +
          "WHERE pdc.donation.donor.id = :donorId " +
          "AND pdc.isDeleted = :isDeleted " +
          "ORDER BY pdc.donation.donationDate ";

  public static final String NAME_COUNT_FLAGGED_POST_DONATION_COUNSELLINGS_FOR_DONOR =
      "PostDonationCounselling.countFlaggedPostDonationCounsellingsForDonor";
  public static final String QUERY_COUNT_FLAGGED_POST_DONATION_COUNSELLINGS_FOR_DONOR =
      "SELECT COUNT(pdc) " +
          "FROM PostDonationCounselling pdc " +
          "WHERE pdc.donation.donor.id = :donorId " +
          "AND pdc.isDeleted = :isDeleted " +
          "AND pdc.flaggedForCounselling = :flaggedForCounselling ";

  public static final String NAME_FIND_POST_DONATION_COUNSELLING_FOR_DONATION =
      "PostDonationCounselling.findPostDonationCounsellingForDonation";
  public static final String QUERY_FIND_POST_DONATION_COUNSELLING_FOR_DONATION =
      "SELECT pdc " +
          "FROM PostDonationCounselling pdc " +
          "WHERE pdc.donation = :donation " +
          "AND pdc.isDeleted = :isDeleted ";
  
  public static final String NAME_FIND_POST_DONATION_COUNSELLINGS_FOR_EXPORT =
      "PostDonationCounselling.findPostDonationCounsellingsForExport";
  public static final String QUERY_FIND_POST_DONATION_COUNSELLINGS_FOR_EXPORT =
      "SELECT NEW org.jembi.bsis.dto.PostDonationCounsellingExportDTO(pdc.donation.donationIdentificationNumber, "
      + "pdc.modificationTracker.createdDate, pdc.modificationTracker.createdBy.username, "
      + "pdc.modificationTracker.lastUpdated, pdc.modificationTracker.lastUpdatedBy.username, pdc.counsellingDate)"
      + "FROM PostDonationCounselling pdc "
      + "WHERE pdc.isDeleted = :deleted "
      + "ORDER BY pdc.modificationTracker.createdDate ASC ";
  
  public static final String NAME_FIND_POST_DONATION_COUNSELLING =
      "PostDonationCounselling.findPostDonationCounselling";
  public static final String QUERY_FIND_POST_DONATION_COUNSELLING =
      "SELECT pdc " +
          "FROM PostDonationCounselling pdc " +
          "WHERE pdc.isDeleted = :isDeleted " +
          "AND pdc.flaggedForCounselling = :flaggedForCounselling " +
          "AND ((:includeReferred = true AND pdc.referred = :referred1) " +
            "OR (:includeReferred = true AND pdc.referred = :referred2) " +
            "OR (:includeReferred = false AND pdc.referred IS NULL)) " +
          "AND (:venuesHasItems = false OR pdc.donation.venue.id IN (:venueIds)) " +
          "AND (:counsellingStatus IS NULL OR pdc.counsellingStatus = :counsellingStatus) " +
          "AND (:startDate IS NULL OR pdc.donation.donationDate >= :startDate) " +
          "AND (:endDate IS NULL OR pdc.donation.donationDate <= :endDate)";
}

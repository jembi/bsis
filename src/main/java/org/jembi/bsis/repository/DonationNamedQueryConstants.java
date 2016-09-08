package org.jembi.bsis.repository;

public class DonationNamedQueryConstants {

  public static final String NAME_COUNT_DONATIONS_FOR_DONOR =
      "Donation.countDonationsForDonor";
  public static final String QUERY_COUNT_DONATION_FOR_DONOR =
      "SELECT COUNT(d) " +
          "FROM Donation d " +
          "WHERE d.donor = :donor " +
          "AND d.isDeleted = :deleted ";

  public static final String NAME_FIND_ASCENDING_DONATION_DATES_FOR_DONOR =
      "Donation.findAscendingDonationDatesForDonor";
  public static final String QUERY_FIND_ASCENDING_DONATION_DATES_FOR_DONOR =
      "SELECT d.donationDate " +
          "FROM Donation d " +
          "WHERE d.donor.id = :donorId " +
          "AND d.isDeleted = :deleted " +
          "ORDER BY d.donationDate ";

  public static final String NAME_FIND_DESCENDING_DONATION_DATES_FOR_DONOR =
      "Donation.findDescendingDonationDatesForDonor";
  public static final String QUERY_FIND_DESCENDING_DONATION_DATES_FOR_DONOR =
      "SELECT d.donationDate " +
          "FROM Donation d " +
          "WHERE d.donor.id = :donorId " +
          "AND d.isDeleted = :deleted " +
          "ORDER BY d.donationDate DESC ";

  public static final String NAME_FIND_COLLECTED_DONATION_VALUE_OBJECTS_FOR_DATE_RANGE =
      "Donation.findCollectedDonationDtosForDateRange";
  public static final String QUERY_FIND_COLLECTED_DONATION_VALUE_OBJECTS_FOR_DATE_RANGE =
      "SELECT NEW org.jembi.bsis.dto.CollectedDonationDTO(d.donationType, do.gender, d.bloodAbo, d.bloodRh, d.venue, COUNT(d)) " +
          "FROM Donation d, Donor do " +
          "WHERE d.donor = do AND d.donationDate BETWEEN :startDate AND :endDate " +
          "AND d.isDeleted = :deleted " +
          "GROUP BY d.venue, do.gender, d.donationType, d.bloodAbo, d.bloodRh " +
          "ORDER BY d.venue, do.gender, d.donationType, d.bloodAbo, d.bloodRh";

  public static final String NAME_FIND_LATEST_DUE_TO_DONATE_DATE_FOR_DONOR =
      "Donation.findLatestDueToDonateDateForDonor";
  public static final String QUERY_FIND_LATEST_DUE_TO_DONATE_DATE_FOR_DONOR =
      "SELECT date_add_days(d.donationDate, d.packType.periodBetweenDonations) AS dueToDonate "
          + "FROM Donation d "
          + "WHERE d.donor.id = :donorId "
          + "AND d.isDeleted = :deleted "
          + "ORDER BY dueToDonate DESC ";
  
  public static final String NAME_FIND_LAST_DONATIONS_BY_DONOR_VENUE_AND_DONATION_DATE =
      "Donation.findLastDonationsByDonorVenueAndDonationDate";
  public static final String QUERY_FIND_LAST_DONATIONS_BY_DONOR_VENUE_AND_DONATION_DATE =
      "SELECT DISTINCT d "
      + "FROM Donation d "
      + "LEFT JOIN FETCH d.bloodTestResults "
      + "WHERE d.donationDate BETWEEN :startDate AND :endDate "
      // Only look at the last donation for each donor
      + "AND d.donationDate = d.donor.dateOfLastDonation "
      + "AND d.donor.venue = :venue "
      + "AND d.isDeleted = :deleted ";
  
  public static final String NAME_FIND_DONATIONS_FOR_EXPORT =
      "Donations.findDonationsForExport";
  /*
   * N.B. It would be preferable to return only the adverse event type name and adverse event comment, but that doesn't
   * seem to be possible. We need to use the full adverse event because accessing the adverse event type name via
   * d.adverseEvent.type.name in the query causes donations without adverse events to be excluded.
   */
  public static final String QUERY_FIND_DONATIONS_FOR_EXPORT =
      "SELECT NEW org.jembi.bsis.dto.DonationExportDTO(d.donor.donorNumber, d.donationIdentificationNumber, "
      + "d.modificationTracker.createdDate, d.modificationTracker.createdBy.username, d.modificationTracker.lastUpdated, "
      + "d.modificationTracker.lastUpdatedBy.username, d.packType.packType, d.donationDate, d.bloodTypingStatus, "
      + "d.bloodTypingMatchStatus, d.ttiStatus, d.bleedStartTime, d.bleedEndTime, d.donorWeight, "
      + "d.bloodPressureSystolic, d.bloodPressureDiastolic, d.donorPulse, d.haemoglobinCount, d.haemoglobinLevel, "
      + "d.adverseEvent, d.bloodAbo, d.bloodRh, d.released, d.ineligibleDonor, d.notes) "
      + "FROM Donation d "
      // Make sure that donations without adverse events are returned
      + "LEFT JOIN d.adverseEvent "
      + "WHERE d.isDeleted = :deleted ";

}

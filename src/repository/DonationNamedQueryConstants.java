package repository;

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
      "SELECT NEW dto.CollectedDonationDTO(d.donationType, d.donor.gender, d.bloodAbo, d.bloodRh, d.donor.venue, COUNT(d)) " +
          "FROM Donation d " +
          "WHERE d.donationDate BETWEEN :startDate AND :endDate " +
          "AND d.isDeleted = :deleted " +
          "GROUP BY d.donor.venue, d.donor.gender, d.donationType, d.bloodAbo, d.bloodRh " +
          "ORDER BY d.donor.venue, d.donor.gender, d.donationType, d.bloodAbo, d.bloodRh";

  public static final String NAME_FIND_LATEST_DUE_TO_DONATE_DATE_FOR_DONOR =
      "Donation.findLatestDueToDonateDateForDonor";
  public static final String QUERY_FIND_LATEST_DUE_TO_DONATE_DATE_FOR_DONOR =
      "SELECT date_add_days(d.donationDate, d.packType.periodBetweenDonations) AS dueToDonate "
          + "FROM Donation d "
          + "WHERE d.donor.id = :donorId "
          + "AND d.isDeleted = :deleted "
          + "ORDER BY dueToDonate DESC ";

}

package repository;

public class BloodTestResultNamedQueryConstants {

  public static final String NAME_COUNT_BLOOD_TEST_RESULTS_FOR_DONATION =
      "BloodTestResult.countBloodTestResultsForDonation";
  public static final String QUERY_COUNT_BLOOD_TEST_RESULTS_FOR_DONATION =
      "SELECT COUNT(btr) " +
          "FROM BloodTestResult btr " +
          "WHERE btr.donation.id = :donationId ";
  
  public static final String NAME_FIND_BLOOD_TEST_RESULT_VALUE_OBJECTS_FOR_DATE_RANGE =
      "BloodTestResult.findBloodTestResultDtosForDateRange";
  public static final String QUERY_FIND_BLOOD_TEST_RESULT_VALUE_OBJECTS_FOR_DATE_RANGE =
      "SELECT NEW dto.BloodTestResultDTO(b.bloodTest, b.result, b.donation.donor.gender, b.donation.donor.venue, COUNT(b)) " +
          "FROM BloodTestResult b " +
          "WHERE b.donation.donationDate BETWEEN :startDate AND :endDate " +
          "AND b.donation.isDeleted = :deleted " +
          "AND b.bloodTest.bloodTestType = :bloodTestType " +
          "AND b.result = :result " +
          "GROUP BY b.donation.donor.venue, b.donation.donor.gender, b.bloodTest, b.result " +
          "ORDER BY b.donation.donor.venue, b.donation.donor.gender, b.bloodTest, b.result";
}

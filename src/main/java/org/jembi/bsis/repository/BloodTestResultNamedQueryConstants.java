package org.jembi.bsis.repository;

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
      "SELECT NEW org.jembi.bsis.dto.BloodTestResultDTO(b.bloodTest, b.result, do.gender, d.venue, COUNT(b)) " +
          "FROM BloodTestResult b, Donation d, Donor do " +
          "WHERE b.donation = d AND d.donor = do AND d.donationDate BETWEEN :startDate AND :endDate " +
          "AND d.isDeleted = :deleted " +
          "AND b.bloodTest.bloodTestType = :bloodTestType " +
          "GROUP BY d.venue, do.gender, b.bloodTest, b.result " +
          "ORDER BY d.venue, do.gender, b.bloodTest, b.result";
}

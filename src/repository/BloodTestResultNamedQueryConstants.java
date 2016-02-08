package repository;

public class BloodTestResultNamedQueryConstants {

  public static final String NAME_COUNT_BLOOD_TEST_RESULTS_FOR_DONATION =
      "BloodTestResult.countBloodTestResultsForDonation";
  public static final String QUERY_COUNT_BLOOD_TEST_RESULTS_FOR_DONATION =
      "SELECT COUNT(btr) " +
          "FROM BloodTestResult btr " +
          "WHERE btr.donation.id = :donationId ";

}

package org.jembi.bsis.repository;

public class BloodTestResultNamedQueryConstants {

  public static final String NAME_COUNT_BLOOD_TEST_RESULTS_FOR_DONATION =
      "BloodTestResult.countBloodTestResultsForDonation";
  public static final String QUERY_COUNT_BLOOD_TEST_RESULTS_FOR_DONATION =
      "SELECT COUNT(btr) " +
          "FROM BloodTestResult btr " +
          "WHERE btr.donation.id = :donationId " +
          "AND btr.isDeleted = :testOutcomeDeleted";
  
  public static final String NAME_FIND_BLOOD_TEST_RESULT_VALUE_OBJECTS_FOR_DATE_RANGE =
      "BloodTestResult.findBloodTestResultDtosForDateRange";
  public static final String QUERY_FIND_BLOOD_TEST_RESULT_VALUE_OBJECTS_FOR_DATE_RANGE =
      "SELECT NEW org.jembi.bsis.dto.BloodTestResultDTO(b.bloodTest, b.result, do.gender, d.venue, COUNT(b)) " +
          "FROM BloodTestResult b, Donation d, Donor do " +
          "WHERE b.donation = d AND d.donor = do AND d.donationDate BETWEEN :startDate AND :endDate " +
          "AND d.isDeleted = :donationDeleted " +
          "AND b.isDeleted = :testOutcomeDeleted " +
          "AND d.released = :released " +
          "AND b.bloodTest.bloodTestType = :bloodTestType " +
          "AND b.bloodTest.isDeleted = :bloodTestDeleted " +
          "AND d.packType.countAsDonation = :countAsDonation " +
          "GROUP BY d.venue, do.gender, b.bloodTest, b.result " +
          "ORDER BY d.venue, do.gender, b.bloodTest, b.result";
  
  public static final String NAME_FIND_TOTAL_UNITS_TESTED_FOR_DATE_RANGE = 
      "BloodTestResult.findTTIPrevalenceReportTotalUnitsTested";
  
  public static final String QUERY_FIND_TOTAL_UNITS_TESTED_FOR_DATE_RANGE = 
      "SELECT NEW org.jembi.bsis.dto.BloodTestTotalDTO(do.gender, d.venue, COUNT(DISTINCT d)) " +
          "FROM BloodTestResult b, Donation d, Donor do " +
          "WHERE b.donation = d AND d.donor = do AND d.donationDate BETWEEN :startDate AND :endDate " +
          "AND d.isDeleted = :donationDeleted " +
          "AND b.isDeleted = :testOutcomeDeleted " +
          "AND d.released = :released " +
          "AND b.bloodTest.bloodTestType = :bloodTestType " +
          "AND d.packType.countAsDonation = :countAsDonation " +
          "GROUP BY d.venue, do.gender " +
          "ORDER BY d.venue, do.gender";
  
  public static final String NAME_FIND_TOTAL_TTI_UNSAFE_UNITS_TESTED_FOR_DATE_RANGE =
      "BloodTestResult.findTTIPrevalenceReportTotalUnsafeUnitsTested";
  
  public static final String QUERY_FIND_TOTAL_TTI_UNSAFE_UNITS_TESTED_FOR_DATE_RANGE =  
      "SELECT NEW org.jembi.bsis.dto.BloodTestTotalDTO(do.gender, d.venue, COUNT(DISTINCT d)) " +
          "FROM BloodTestResult b, Donation d, Donor do " +
          "WHERE b.donation = d AND d.donor = do AND d.donationDate BETWEEN :startDate AND :endDate " +
          "AND d.isDeleted = :donationDeleted " +
          "AND b.isDeleted = :testOutcomeDeleted " +
          "AND d.released = :released " +
          "AND b.bloodTest.bloodTestType = :bloodTestType " +
          "AND d.ttiStatus = :ttiStatus " +
          "AND d.packType.countAsDonation = :countAsDonation " +
          "GROUP BY d.venue, do.gender " +
          "ORDER BY d.venue, do.gender";
  
  public static final String NAME_GET_TEST_OUTCOMES_FOR_DONATION =
      "BloodTestResult.getTestOutcomes";
  
  public static final String QUERY_GET_TEST_OUTCOMES_FOR_DONATION =  
      "SELECT DISTINCT btr FROM BloodTestResult btr, Donation d " +
      "WHERE btr.donation = :donation " +
      "AND btr.isDeleted = :testOutcomeDeleted";
  
  public static final String NAME_FIND_BLOOD_TEST_RESULTS_FOR_EXPORT =
      "BloodTestResult.findBloodTestResultsForExport";
  public static final String QUERY_FIND_BLOOD_TEST_RESULTS_FOR_EXPORT =
      "SELECT NEW org.jembi.bsis.dto.BloodTestResultExportDTO(btr.donation.donationIdentificationNumber, "
      + "btr.modificationTracker.createdDate, btr.modificationTracker.createdBy.username, "
      + "btr.modificationTracker.lastUpdated, btr.modificationTracker.lastUpdatedBy.username, "
      + "btr.bloodTest.testNameShort, btr.result) "
      + "FROM BloodTestResult btr "
      + "WHERE btr.isDeleted = :deleted "
      + "ORDER BY btr.modificationTracker.createdDate ASC ";
}

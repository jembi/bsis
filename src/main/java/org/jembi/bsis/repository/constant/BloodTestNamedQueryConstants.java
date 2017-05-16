package org.jembi.bsis.repository.constant;

public class BloodTestNamedQueryConstants {

  public static final String NAME_GET_BLOOD_TESTS_BY_CATEGORY = 
      "BloodTest.getBloodTestsByCategory";
  public static final String QUERY_GET_BLOOD_TESTS_BY_CATEGORY =
      "SELECT b FROM BloodTest b "
      + "WHERE b.category = :category "
      + "AND b.isActive = :isActive "
      + "AND b.isDeleted = :isDeleted "
      + "ORDER BY b.rankInCategory ASC NULLS LAST";

  public static final String NAME_GET_BLOOD_TESTS_BY_TYPE = 
      "BloodTest.getBloodTestsByType";
  public static final String QUERY_GET_BLOOD_TESTS_BY_TYPE =
      "SELECT b FROM BloodTest b "
      + "WHERE b.bloodTestType IN (:types) "
      + "AND (:isActive = null OR b.isActive = :isActive) "
      + "AND b.isDeleted = :isDeleted "
      + "ORDER BY b.rankInCategory ASC NULLS LAST";

  public static final String NAME_GET_BLOOD_TESTS = 
      "BloodTest.getBloodTests";
  public static final String QUERY_GET_BLOOD_TESTS =
      "SELECT b FROM BloodTest b "
      + "WHERE (:includeDeleted = TRUE OR b.isDeleted = FALSE) "
      + "AND (:includeInactive = TRUE OR b.isActive = TRUE) "
      + "ORDER BY b.category, b.rankInCategory ASC NULLS LAST";

  public static final String NAME_FIND_BLOOD_TEST_BY_ID =
      "BloodTest.findBloodTestById";
  public static final String QUERY_FIND_BLOOD_TEST_BY_ID =
      "SELECT bt FROM BloodTest bt WHERE bt.id=:bloodTestId";

  public static final String NAME_VERIFY_UNIQUE_BLOOD_TEST =
      "BloodTest.verifyUniqueBloodTest";
  public static final String QUERY_VERIFY_UNIQUE_BLOOD_TEST =
      "SELECT count(b) = 0 "
      + "FROM BloodTest b "
      + "WHERE b.testName = :testName "
      + " AND (:includeId = false OR b.id != :id)";
  
  public static final String NAME_VERIFY_BLOOD_TEST_WITH_ID_EXISTS =
      "BloodTest.verifyExists";
  public static final String QUERY_VERIFY_BLOOD_TEST_WITH_ID_EXISTS =
      "SELECT count(b) > 0 " 
          + "FROM BloodTest b " 
          + "WHERE b.id = :id ";
}

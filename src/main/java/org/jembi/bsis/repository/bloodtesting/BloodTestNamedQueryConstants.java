package org.jembi.bsis.repository.bloodtesting;

public class BloodTestNamedQueryConstants {

  public static final String NAME_GET_BLOOD_TESTS_BY_CATEGORY = 
      "BloodTest.getBloodTestsByCategory";
  public static final String QUERY_GET_BLOOD_TESTS_BY_CATEGORY =
      "SELECT b FROM BloodTest b "
      + "WHERE b.category = :category "
      + "AND b.isActive = :isActive";

  public static final String NAME_GET_BLOOD_TESTS_BY_TYPE = 
      "BloodTest.getBloodTestsByType";
  public static final String QUERY_GET_BLOOD_TESTS_BY_TYPE =
      "SELECT b FROM BloodTest b "
      + "WHERE b.bloodTestType IN (:types) "
      + "AND b.isActive = :isActive";

  public static final String NAME_GET_BLOOD_TESTS = 
      "BloodTest.getBloodTests";
  public static final String QUERY_GET_BLOOD_TESTS =
      "SELECT b FROM BloodTest b "
      + "WHERE (:includeDeleted = TRUE OR b.isDeleted = FALSE) "
      + "AND (:includeInactive = TRUE OR b.isActive = TRUE)";
  
}

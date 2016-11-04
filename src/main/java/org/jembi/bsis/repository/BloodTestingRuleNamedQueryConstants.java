package org.jembi.bsis.repository;

public class BloodTestingRuleNamedQueryConstants {

  public static final String NAME_GET_BLOOD_TESTING_RULES =
      "BloodTestingRule.getBloodTestingRules";
  public static final String QUERY_GET_BLOOD_TESTING_RULES = 
      "SELECT btr " 
      + "FROM BloodTestingRule btr "
      + "WHERE (:includeDeleted = TRUE OR btr.isDeleted = FALSE)";
}

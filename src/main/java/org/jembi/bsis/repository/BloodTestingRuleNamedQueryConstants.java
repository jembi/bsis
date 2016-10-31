package org.jembi.bsis.repository;

public class BloodTestingRuleNamedQueryConstants {

  public static final String NAME_GET_ACTIVE_BLOOD_TESTING_RULES =
      "BloodTestingRule.getActiveBloodTestingRules";
  public static final String QUERY_GET_ACTIVE_BLOOD_TESTING_RULES = 
      "SELECT btr " 
      + "FROM BloodTestingRule btr "
      + "WHERE (:includeInactive = TRUE OR btr.isActive = TRUE)";
}

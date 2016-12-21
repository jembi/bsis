package org.jembi.bsis.repository;

public class BloodTestingRuleNamedQueryConstants {

  public static final String NAME_GET_BLOOD_TESTING_RULES =
      "BloodTestingRule.getBloodTestingRules";
  public static final String QUERY_GET_BLOOD_TESTING_RULES = 
      "SELECT btr " 
      + "FROM BloodTestingRule btr "
      + "WHERE (:includeDeleted = TRUE OR btr.isDeleted = FALSE)";
  
  public static final String NAME_FIND_BLOOD_TESTING_RULE_BY_ID = 
      "BloodTestingRule.findBloodTestingRuleById";
  public static final String QUERY_FIND_BLOOD_TESTING_RULE_BY_ID = 
      "SELECT btr FROM BloodTestingRule btr WHERE btr.id=:bloodTestingRuleId";
}

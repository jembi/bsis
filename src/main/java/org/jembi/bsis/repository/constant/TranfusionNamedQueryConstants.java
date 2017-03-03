package org.jembi.bsis.repository.constant;

public class TranfusionNamedQueryConstants {
  
  public static final String NAME_FIND_TRANSFUSION_BY_DIN_AND_COMPONENT_CODE =
      "Component.findTransfusionByDINAndCode";
  public static final String QUERY_FIND_TRANSFUSION_BY_DIN_AND_COMPONENT_CODE =
      "SELECT t "
      + "FROM Transfusion t "
      + "LEFT JOIN t.component.donation AS d "
      + "WHERE (d.donationIdentificationNumber = :donationIdentificationNumber "
      + "OR CONCAT(d.donationIdentificationNumber, d.flagCharacters) = :donationIdentificationNumber) " 
      + "AND t.component.componentCode = :componentCode AND t.isDeleted = :isDeleted";
}

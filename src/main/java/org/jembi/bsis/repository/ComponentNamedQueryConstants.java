package org.jembi.bsis.repository;


public class ComponentNamedQueryConstants {

  public static final String NAME_COUNT_CHANGED_COMPONENTS_FOR_DONATION =
      "Component.countChangedComponentsForDonation";
  public static final String QUERY_COUNT_CHANGED_COMPONENTS_FOR_DONATION =
      "SELECT COUNT(c) " +
          "FROM Component c " +
          "WHERE c.donation.id = :donationId " +
          "AND c.isDeleted = :deleted " +
          // Has a parent component
          "AND (c.parentComponent IS NOT NULL " +
          // Has a status other than the initial status
          " OR c.status != :initialStatus " +
          // Has status changes
          " OR c.statusChanges IS NOT EMPTY) ";

  public static final String NAME_UPDATE_COMPONENT_STATUSES_FOR_DONOR =
      "Component.updateComponentStatusesForDonor";
  public static final String QUERY_UPDATE_COMPONENT_STATUSES_FOR_DONOR =
      "UPDATE Component c " +
          "SET c.status = :newStatus " +
          "WHERE c.status IN :oldStatuses " +
          "AND c.donation IN (" +
          "  SELECT d " +
          "  FROM Donation d " +
          "  WHERE d.donor = :donor " +
          ") ";

  public static final String NAME_UPDATE_COMPONENT_STATUSES_FOR_DONATION =
      "Component.updateComponentStatusesForDonation";
  public static final String QUERY_UPDATE_COMPONENT_STATUSES_FOR_DONATION =
      "UPDATE Component c " +
          "SET c.status = :newStatus " +
          "WHERE c.status IN :oldStatuses " +
          "AND c.donation = :donation ";

  public static final String NAME_FIND_COMPONENTS_BY_DIN =
      "Component.findComponentsByDIN";
  public static final String QUERY_FIND_COMPONENTS_BY_DIN =
      "SELECT DISTINCT c FROM Component c " + 
          "WHERE c.donation.donationIdentificationNumber = :donationIdentificationNumber " +
          "AND c.isDeleted= :isDeleted";
  
  public static final String NAME_FIND_COMPONENT_BY_CODE_AND_DIN =
      "Component.findComponentByCodeAndDIN";
  public static final String QUERY_FIND_COMPONENT_BY_CODE_AND_DIN =
      "SELECT c "
      + "FROM Component c "
      + "WHERE c.donation.donationIdentificationNumber = :donationIdentificationNumber "
      + "AND c.componentCode = :componentCode ";
  
  public static final String NAME_FIND_COMPONENT_BY_CODE_AND_DIN_IN_STOCK =
      "Component.findComponentByCodeAndDINInStock";
  public static final String QUERY_FIND_COMPONENT_BY_CODE_AND_DIN_IN_STOCK =
      "SELECT c "
      + "FROM Component c "
      + "WHERE c.donation.donationIdentificationNumber = :donationIdentificationNumber "
      + "AND c.componentCode = :componentCode "
      + "AND c.inventoryStatus = 'IN_STOCK' ";

  public static final String NAME_COUNT_COMPONENT_WITH_ID = "Component.countComponentsWithId";
  public static final String QUERY_COUNT_COMPONENT_WITH_ID =
      "SELECT count(*) FROM Component c WHERE c.id=:id AND c.isDeleted = false";
  
}
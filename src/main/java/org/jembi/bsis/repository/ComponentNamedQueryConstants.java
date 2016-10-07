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

  public static final String NAME_FIND_COMPONENTS_BY_DIN =
      "Component.findComponentsByDIN";
  public static final String QUERY_FIND_COMPONENTS_BY_DIN =
      "SELECT DISTINCT c FROM Component c " + 
          "WHERE c.donation.donationIdentificationNumber = :donationIdentificationNumber " +
          "AND c.isDeleted = :isDeleted";

  public static final String NAME_FIND_COMPONENTS_BY_DIN_AND_STATUS =
      "Component.findComponentsByDINAndStatus";
  public static final String QUERY_FIND_COMPONENTS_BY_DIN_AND_STATUS =
      "SELECT DISTINCT c FROM Component c " + 
          "WHERE c.donation.donationIdentificationNumber = :donationIdentificationNumber " +
          "AND c.status = :status " +
          "AND c.isDeleted = :isDeleted";
  
  public static final String NAME_FIND_COMPONENT_BY_CODE_AND_DIN =
      "Component.findComponentByCodeAndDIN";
  public static final String QUERY_FIND_COMPONENT_BY_CODE_AND_DIN =
      "SELECT c "
      + "FROM Component c "
      + "WHERE c.donation.donationIdentificationNumber = :donationIdentificationNumber "
      + "AND c.componentCode = :componentCode AND c.isDeleted = false ";
  
  public static final String NAME_FIND_COMPONENT_BY_CODE_AND_DIN_IN_STOCK =
      "Component.findComponentByCodeAndDINInStock";
  public static final String QUERY_FIND_COMPONENT_BY_CODE_AND_DIN_IN_STOCK =
      "SELECT c "
      + "FROM Component c "
      + "WHERE c.donation.donationIdentificationNumber = :donationIdentificationNumber "
      + "AND c.componentCode = :componentCode "
      + "AND c.inventoryStatus = 'IN_STOCK' AND c.isDeleted = false ";

  public static final String NAME_COUNT_COMPONENT_WITH_ID = "Component.countComponentsWithId";
  public static final String QUERY_COUNT_COMPONENT_WITH_ID =
      "SELECT count(*) FROM Component c WHERE c.id=:id AND c.isDeleted = false";
  
  public static final String NAME_FIND_CHILD_COMPONENTS = "Component.findChildComponents";
  public static final String QUERY_FIND_CHILD_COMPONENTS =
      "SELECT c FROM Component c WHERE c.parentComponent=:parentComponent AND c.isDeleted = false";

  public static final String NAME_FIND_COMPONENTS_FOR_EXPORT =
      "Component.findComponentsForExport";
  public static final String QUERY_FIND_COMPONENTS_FOR_EXPORT =
      "SELECT DISTINCT NEW org.jembi.bsis.dto.ComponentExportDTO(c.id, c.donation.donationIdentificationNumber, "
      + "c.componentCode, c.modificationTracker.createdDate, c.modificationTracker.createdBy.username, "
      + "c.modificationTracker.lastUpdated, c.modificationTracker.lastUpdatedBy.username, "
      + "c.parentComponent.componentCode, c.createdOn, c.status, c.location.name, c.issuedOn, c.inventoryStatus, "
      + "c.discardedOn, r.statusChangeReason, c.expiresOn, c.notes) "
      + "FROM Component c "
      // Make sure that components without parents are returned
      + "LEFT JOIN c.parentComponent "
      // Join to status change to get status change reason
      + "LEFT JOIN c.statusChanges AS sc "
      + "WITH sc.isDeleted = :statusChangeDeleted "
      // Join to status change reason to find discarded
      + "LEFT JOIN sc.statusChangeReason AS r "
      + "WITH r.category = :discarded "
      + "WHERE c.isDeleted = :componentDeleted "
      // Sort by created date then status change reason with nulls last so that discards come first
      + "ORDER BY c.modificationTracker.createdDate ASC, r.statusChangeReason ASC NULLS LAST ";

  public static final String NAME_FIND_SUMMARY_FOR_DISCARDED_COMPONENTS_BY_VENUE =
      "Component.findDiscardedComponentsByVenue";
  public static final String QUERY_FIND_SUMMARY_FOR_DISCARDED_COMPONENTS_BY_VENUE =
      "select DISTINCT new org.jembi.bsis.dto.DiscardedComponentDTO(s.component.componentType.componentTypeName, s.statusChangeReason.statusChangeReason, s.component.componentBatch.location, count(s.component)) " +
      "from ComponentStatusChange AS s " +
      "where s.component.status = 'DISCARDED' and s.newStatus ='DISCARDED' " +
      "and s.component.componentBatch.location.id =:venueId " +
      "and s.isDeleted = false " +
      "and s.statusChangedOn BETWEEN :startDate AND :endDate " +
      "group by s.component.componentBatch.location, s.component.componentType.componentTypeName, s.statusChangeReason.statusChangeReason " +
      "order by s.component.componentBatch.location, s.component.componentType.componentTypeName desc ";

  public static final String NAME_FIND_SUMMARY_FOR_ALL_DISCARDED_COMPONENTS =
      "Component.findAllDiscardedComponents";
  public static final String QUERY_FIND_SUMMARY_FOR_ALL_DISCARDED_COMPONENTS =
      "select DISTINCT new org.jembi.bsis.dto.DiscardedComponentDTO(s.component.componentType.componentTypeName, s.statusChangeReason.statusChangeReason, s.component.componentBatch.location, count(s.component)) " +
      "from ComponentStatusChange AS s " +
      "where s.component.status = 'DISCARDED' and s.newStatus ='DISCARDED' " +
      "and s.statusChangedOn BETWEEN :startDate AND :endDate " +
      "and s.isDeleted = false " +
      "group by s.component.componentBatch.location, s.component.componentType.componentTypeName, s.statusChangeReason.statusChangeReason " +
      "order by s.component.componentBatch.location, s.component.componentType.componentTypeName desc ";
}

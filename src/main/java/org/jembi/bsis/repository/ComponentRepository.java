package org.jembi.bsis.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.jembi.bsis.dto.ComponentExportDTO;
import org.jembi.bsis.dto.ComponentProductionDTO;
import org.jembi.bsis.dto.DiscardedComponentDTO;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.model.util.BloodGroup;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ComponentRepository extends AbstractRepository<Component> {

  @PersistenceContext
  private EntityManager em;

  public List<Component> findAnyComponent(List<UUID> componentTypes, ComponentStatus status,
      Date donationDateFrom, Date donationDateTo, UUID locationId) {

    boolean includeComponentTypes = true, includeStatus = true;
    if (status == null) {
      includeStatus = false;
    }
    if (componentTypes == null) {
      includeComponentTypes = false;
    }

    boolean includeAllLocations = false;
    if (locationId == null) {
      includeAllLocations = true;
    }

    return em.createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_ANY_COMPONENT, Component.class)
          .setParameter("isDeleted", Boolean.FALSE)
          .setParameter("status", status)
          .setParameter("locationId", locationId)
          .setParameter("includeAllLocations", includeAllLocations)
          .setParameter("componentTypeIds", componentTypes)
          .setParameter("donationDateFrom", donationDateFrom)
          .setParameter("donationDateTo", donationDateTo)
          .setParameter("includeStatus", includeStatus)
          .setParameter("includeComponentTypes", includeComponentTypes)
          .getResultList();
  }

  public List<Component> findComponentsByDonationIdentificationNumber(String donationIdentificationNumber) {
    return em.createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_COMPONENTS_BY_DIN, Component.class)
        .setParameter("isDeleted", Boolean.FALSE)
        .setParameter("donationIdentificationNumber", donationIdentificationNumber)
        .getResultList();
  }

  public List<Component> findComponentsByDonationIdentificationNumberAndStatus(String donationIdentificationNumber,
      ComponentStatus status) {
    return em.createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_COMPONENTS_BY_DIN_AND_STATUS, Component.class)
        .setParameter("isDeleted", Boolean.FALSE)
        .setParameter("donationIdentificationNumber", donationIdentificationNumber)
        .setParameter("status", status)
        .getResultList();
  }

  public Component findComponent(UUID componentId) {
    return em.find(Component.class, componentId);
  }

  public Component findComponentById(UUID componentId) throws NoResultException {
    String queryString = "SELECT c FROM Component c LEFT JOIN FETCH c.donation where c.id = :componentId AND c.isDeleted = :isDeleted";
    TypedQuery<Component> query = em.createQuery(queryString, Component.class);
    query.setParameter("isDeleted", Boolean.FALSE);
    query.setParameter("componentId", componentId);
    Component component = null;
    component = query.getSingleResult();
    return component;
  }

  public void updateExpiryStatus() {
    String updateExpiryQuery = "UPDATE Component c SET c.status=:status WHERE " +
        "c.status=:availableStatus AND " +
        "c.expiresOn < :today";
    Query query = em.createQuery(updateExpiryQuery);
    query.setParameter("status", ComponentStatus.EXPIRED);
    query.setParameter("availableStatus", ComponentStatus.AVAILABLE);
    query.setParameter("today", new Date());
    query.executeUpdate();
  }

  public List<Component> findComponentsByDINAndType(String donationIdentificationNumber, UUID componentTypeId) {
    String queryStr = "SELECT c from Component c WHERE " +
        "(c.donation.donationIdentificationNumber = :donationIdentificationNumber " +
        "OR CONCAT(c.donation.donationIdentificationNumber, c.donation.flagCharacters) = :donationIdentificationNumber) AND " +
        "c.componentType.id = :componentTypeId AND c.isDeleted = false";
    TypedQuery<Component> query = em.createQuery(queryStr, Component.class);
    query.setParameter("donationIdentificationNumber", donationIdentificationNumber);
    query.setParameter("componentTypeId", componentTypeId);
    return query.getResultList();
  }

  // TODO: Test
  public int countChangedComponentsForDonation(UUID donationId) {
    return em.createNamedQuery(
        ComponentNamedQueryConstants.NAME_COUNT_CHANGED_COMPONENTS_FOR_DONATION,
        Number.class)
        .setParameter("donationId", donationId)
        .setParameter("deleted", false)
        .setParameter("initialStatus", ComponentStatus.QUARANTINED)
        .getSingleResult()
        .intValue();
  }
  
  public Component findComponentByCodeAndDIN(String componentCode, String donationIdentificationNumber) {
    return em.createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_COMPONENT_BY_CODE_AND_DIN, Component.class)
        .setParameter("donationIdentificationNumber", donationIdentificationNumber)
        .setParameter("componentCode", componentCode)
        .getSingleResult();
  }

  public boolean verifyComponentExists(UUID id) {
    Long count = em.createNamedQuery(ComponentNamedQueryConstants.NAME_COUNT_COMPONENT_WITH_ID, Long.class)
        .setParameter("id", id).getSingleResult();
    if (count == 1) {
      return true;
    }
    return false;
  }

  public List<Component> findChildComponents(Component parentComponent) {
    return em.createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_CHILD_COMPONENTS, Component.class)
        .setParameter("parentComponent", parentComponent)
        .getResultList();
  }
  
  public Set<ComponentExportDTO> findComponentsForExport() {
    List<ComponentExportDTO> componentExportDTOs = em.createNamedQuery(
        ComponentNamedQueryConstants.NAME_FIND_COMPONENTS_FOR_EXPORT, ComponentExportDTO.class)
        .setParameter("discarded", ComponentStatusChangeReasonCategory.DISCARDED)
        .setParameter("statusChangeDeleted", false)
        .setParameter("componentDeleted", false)
        .getResultList();
    
    /*
     * As a result of joining on status change multiple rows may be returned for the same component.
     * Adding the rows to a set makes sure that each component only has one row.
     * The rows with discard reasons are sorted ahead of those without so that they added to the set first.
     */
    return new LinkedHashSet<>(componentExportDTOs);
  }

  public List<DiscardedComponentDTO> findSummaryOfDiscardedComponentsByProcessingSite(UUID processingSiteId,
      Date starDate, Date endDate) {

    boolean includeAllProcessingSites = false;
    if (processingSiteId == null) {
      includeAllProcessingSites = true;
    }

    return em.createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_SUMMARY_FOR_DISCARDED_COMPONENTS_BY_PROCESSING_SITE, DiscardedComponentDTO.class)
        .setParameter("processingSiteId", processingSiteId)
        .setParameter("includeAllProcessingSites", includeAllProcessingSites)
        .setParameter("startDate", starDate)
        .setParameter("endDate", endDate)
        .getResultList();
  }
  
  public List<ComponentProductionDTO> findProducedComponentsByProcessingSite(UUID processingSiteId, Date startDate,
      Date endDate) {

    boolean includeAllProcessingSites = false;
    if (processingSiteId == null) {
      includeAllProcessingSites = true;
    }
    return em.createNamedQuery(
        ComponentNamedQueryConstants.NAME_FIND_PRODUCED_COMPONENTS_BY_PROCESSING_SITE, ComponentProductionDTO.class)
        .setParameter("processingSiteId", processingSiteId)
        .setParameter("includeAllProcessingSites", includeAllProcessingSites)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("excludedStatuses", Arrays.asList(ComponentStatus.PROCESSED))
        .setParameter("deleted",false)
        .getResultList();
  }

  public List<Component> findSafeComponents(UUID componentTypeId, UUID locationId, List<BloodGroup> bloodGroups,
      Date startDate, Date endDate, List<InventoryStatus> inventoryStatuses, boolean includeInitialComponents) {
    boolean includeBloodGroups = true;
    List<String> stringBloodGroups = null;

    if(bloodGroups == null || bloodGroups.isEmpty()) {
      includeBloodGroups = false;
    } else {
      stringBloodGroups = new ArrayList<>();
      for(BloodGroup bloodGroup: bloodGroups) {
        stringBloodGroups.add(bloodGroup.getBloodAbo()+bloodGroup.getBloodRh());
      }
    }

    boolean includeAllLocations = false;
    if (locationId == null) {
      includeAllLocations = true;
    }

    boolean includeAllComponents = false;
    if (componentTypeId == null) {
      includeAllComponents = true;
    }

    return em.createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_SAFE_COMPONENTS, Component.class)
        .setParameter("locationId", locationId)
        .setParameter("includeAllLocations", includeAllLocations)
        .setParameter("componentTypeId", componentTypeId)
        .setParameter("includeAllComponents", includeAllComponents)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("includeBloodGroups", includeBloodGroups)
        .setParameter("bloodGroups", stringBloodGroups)
        .setParameter("inventoryStatuses",inventoryStatuses)
        .setParameter("isDeleted", false)
        .setParameter("includeInitialComponents", includeInitialComponents)
        .getResultList();
  }

  /**
   * Finds Components by DIN, Component Code and Status. If ComponentCode and/or Status is null it
   * is not used in the filter criteria.
   * 
   * @param donationIdentificationNumber
   * @param componentCode
   * @param status
   * @return
   */
  public List<Component> findComponentsByDINAndComponentCodeAndStatus(String donationIdentificationNumber,
      String componentCode, ComponentStatus status, boolean includeInitialComponents) {

    boolean includeAllComponentCodes = false;
    boolean includeAllComponentStatuses = false;

    if (componentCode == null) {
      includeAllComponentCodes = true;
    }

    if (status == null) {
      includeAllComponentStatuses = true;
    }

    return em
        .createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_COMPONENTS_BY_DIN_AND_COMPONENT_CODE_AND_STATUS, Component.class)
        .setParameter("donationIdentificationNumber", donationIdentificationNumber)
        .setParameter("includeAllComponentCodes", includeAllComponentCodes)
        .setParameter("componentCode", componentCode)
        .setParameter("includeAllComponentStatuses", includeAllComponentStatuses)
        .setParameter("status", status)
        .setParameter("isDeleted", Boolean.FALSE)
        .setParameter("includeInitialComponents", includeInitialComponents)
        .getResultList();
  }
}

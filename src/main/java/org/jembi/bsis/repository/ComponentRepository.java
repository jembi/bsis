package org.jembi.bsis.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

  public List<Component> findAnyComponent(List<Long> componentTypes, ComponentStatus status,
      Date donationDateFrom, Date donationDateTo, Long locationId) {

    boolean includeComponentTypes = true, includeStatus = true;
    if (status == null) {
      includeStatus = false;
    }
    if (componentTypes == null) {
      includeComponentTypes = false;
    }

    return em.createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_ANY_COMPONENT, Component.class)
          .setParameter("isDeleted", Boolean.FALSE)
          .setParameter("status", status)
          .setParameter("locationId", locationId)
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

  public Component findComponent(Long componentId) {
    return em.find(Component.class, componentId);
  }

  public Component findComponentById(Long componentId) throws NoResultException {
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

  public List<Component> findComponentsByDINAndType(String donationIdentificationNumber, long componentTypeId) {
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
  public int countChangedComponentsForDonation(long donationId) {
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

  public boolean verifyComponentExists(Long id) {
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

  public List<DiscardedComponentDTO> findSummaryOfDiscardedComponentsByProcessingSite(Long processingSiteId, Date starDate, Date endDate) {
    return em.createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_SUMMARY_FOR_DISCARDED_COMPONENTS_BY_PROCESSING_SITE, DiscardedComponentDTO.class)
        .setParameter("processingSiteId", processingSiteId)
        .setParameter("startDate", starDate)
        .setParameter("endDate", endDate)
        .getResultList();
  }
  
  public List<ComponentProductionDTO> findProducedComponentsByProcessingSite(Long processingSiteId, Date startDate, Date endDate) {
    return em.createNamedQuery(
        ComponentNamedQueryConstants.NAME_FIND_PRODUCED_COMPONENTS_BY_PROCESSING_SITE, ComponentProductionDTO.class)
        .setParameter("processingSiteId", processingSiteId)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("excludedStatuses", Arrays.asList(ComponentStatus.PROCESSED))
        .setParameter("deleted",false)
        .getResultList();
  }

  public List<Component> findSafeComponents(Long componentTypeId, Long locationId,
      List<BloodGroup> bloodGroups, Date startDate, Date endDate, InventoryStatus inventoryStatus) {
    boolean includeBloodGroups = true, includePositiveAbos = true, includeNegativeAbos = true;

    List<String> negativeBloodAbos = new ArrayList<>();
    List<String> positiveBloodAbos = new ArrayList<>();
    if(bloodGroups == null || bloodGroups.isEmpty()) {
      includeBloodGroups = false;
    } else {
      for (BloodGroup bloodGroup : bloodGroups) {
        if (bloodGroup.getBloodRh() == "-") {
          negativeBloodAbos.add(bloodGroup.getBloodAbo());
        } else {
          positiveBloodAbos.add(bloodGroup.getBloodAbo());
        }
      }
    }
    if (negativeBloodAbos == null || negativeBloodAbos.isEmpty()) {
      includeNegativeAbos = false;
      negativeBloodAbos = null;
    }
    if (positiveBloodAbos == null || positiveBloodAbos.isEmpty()) {
      includePositiveAbos = false;
      positiveBloodAbos = null;
    }

    return em.createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_SAFE_COMPONENTS, Component.class)
        .setParameter("locationId", locationId)
        .setParameter("componentTypeId", componentTypeId)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("includeBloodGroups", includeBloodGroups)
        .setParameter("inventoryStatus",inventoryStatus)
        .setParameter("includeNegativeAbos", includeNegativeAbos)
        .setParameter("includePositiveAbos", includePositiveAbos)
        .setParameter("negativeBloodAbos", negativeBloodAbos)
        .setParameter("positiveBloodAbos", positiveBloodAbos)
        .setParameter("isDeleted", false)
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
      String componentCode, ComponentStatus status) {

    boolean includeAllComponentTypes = false;
    boolean includeAllComponentStatuses = false;

    if (componentCode == null) {
      includeAllComponentTypes = true;
    }

    if (status == null) {
      includeAllComponentStatuses = true;
    }

    return em
        .createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_COMPONENTS_BY_DIN_AND_COMPONENT_CODE_AND_STATUS, Component.class)
        .setParameter("donationIdentificationNumber", donationIdentificationNumber)
        .setParameter("includeAllComponentTypes", includeAllComponentTypes)
        .setParameter("componentCode", componentCode)
        .setParameter("status", status)
        .setParameter("includeAllComponentStatuses", includeAllComponentStatuses)
        .setParameter("isDeleted", Boolean.FALSE)
        .getResultList();
  }
}

package repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dto.StockLevelDTO;
import model.component.Component;
import model.inventory.InventoryStatus;

@Repository
@Transactional
public class InventoryRepository {

  @PersistenceContext
  private EntityManager em;

  public List<StockLevelDTO> findStockLevelsForLocation(Long locationId, InventoryStatus inventoryStatus) {
    return em.createNamedQuery(InventoryNamedQueryConstants.NAME_FIND_STOCK_LEVELS_FOR_LOCATION,
        StockLevelDTO.class).setParameter("locationId", locationId).setParameter("deleted", false)
        .setParameter("inventoryStatus", inventoryStatus).getResultList();
  }

  public List<StockLevelDTO> findStockLevels(InventoryStatus inventoryStatus) {
    return em.createNamedQuery(InventoryNamedQueryConstants.NAME_FIND_STOCK_LEVELS, StockLevelDTO.class)
        .setParameter("deleted", false).setParameter("inventoryStatus", inventoryStatus).getResultList();
  }
  
  public Component findComponentByCodeAndDINInStock(String componentCode, String donationIdentificationNumber) {
    return em.createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_COMPONENT_BY_CODE_AND_DIN_IN_STOCK, Component.class)
        .setParameter("donationIdentificationNumber", donationIdentificationNumber)
        .setParameter("componentCode", componentCode)
        .setParameter("inventoryStatus", InventoryStatus.IN_STOCK)
        .getSingleResult();
  }

  public List<Component> findComponentsInStock(Long locationId, Long componentTypeId, Date dueToExpireBy,
      String bloodAbo, String bloodRh) {

    String queryString =
        "SELECT c FROM Component c WHERE c.isDeleted = :isDeleted AND c.inventoryStatus = :inventoryStatus ";
    if (locationId != null) {
      queryString = queryString + "AND c.location.id = :locationId ";
    }
    if (componentTypeId != null) {
      queryString = queryString + "AND c.componentType.id = :componentTypeId ";
    }
    if (dueToExpireBy != null) {
      queryString = queryString + "AND c.expiresOn <= :dueToExpireBy ";
    }
    if (StringUtils.isNotEmpty(bloodAbo)) {
      queryString = queryString + "AND c.donation.bloodAbo = :bloodAbo ";
    }
    if (StringUtils.isNotEmpty(bloodRh)) {
      queryString = queryString + "AND c.donation.bloodRh = :bloodRh ";
    }

    TypedQuery<Component> query = em.createQuery(queryString, Component.class);
    query.setParameter("isDeleted", false);
    query.setParameter("inventoryStatus", InventoryStatus.IN_STOCK);

    if (locationId != null) {
      query.setParameter("locationId", locationId);
    }
    if (componentTypeId != null) {
      query.setParameter("componentTypeId", componentTypeId);
    }
    if (dueToExpireBy != null) {
      query.setParameter("dueToExpireBy", dueToExpireBy);
    }
    if (StringUtils.isNotEmpty(bloodAbo)) {
      query.setParameter("bloodAbo", bloodAbo);
    }
    if (StringUtils.isNotEmpty(bloodRh)) {
      query.setParameter("bloodRh", bloodRh);
    }

    return query.getResultList();
  }
}

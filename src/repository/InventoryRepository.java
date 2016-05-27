package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
  
  public Component findComponentByCodeDINAndInventoryStatus(String componentCode, String donationIdentificationNumber, InventoryStatus inventoryStatus) {
    return em.createNamedQuery(ComponentNamedQueryConstants.NAME_FIND_COMPONENT_BY_CODE_DIN_AND_INVENTORY_STATUS, Component.class)
        .setParameter("donationIdentificationNumber", donationIdentificationNumber)
        .setParameter("componentCode", componentCode)
        .setParameter("inventoryStatus", inventoryStatus)
        .getSingleResult();
  }
}

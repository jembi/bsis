package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dto.StockLevelDTO;
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
}

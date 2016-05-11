package repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dto.StockLevelDTO;
import model.inventory.InventoryStatus;
import model.location.Location;

@Repository
@Transactional
public class InventoryRepository {

  @PersistenceContext
  private EntityManager em;

  public List<StockLevelDTO> findStockLevelsForLocation(Location location, InventoryStatus inventoryStatus) {
    return em.createNamedQuery(InventoryNamedQueryConstants.NAME_FIND_STOCK_LEVELS_FOR_LOCATION,
        StockLevelDTO.class).setParameter("location", location).setParameter("deleted", false)
        .setParameter("inventoryStatus", inventoryStatus).getResultList();
  }
}

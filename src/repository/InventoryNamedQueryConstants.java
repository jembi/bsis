package repository;

public class InventoryNamedQueryConstants {

  public static final String NAME_FIND_STOCK_LEVELS_FOR_LOCATION = "Component.findStockLevelsForLocation";
  public static final String QUERY_FIND_STOCK_LEVELS_FOR_LOCATION =
      "SELECT NEW dto.StockLevelDTO(c.componentType, c.donation.bloodAbo, c.donation.bloodRh, COUNT(c)) " +
      "FROM Component c " +
      "WHERE c.location.id = :locationId AND c.inventoryStatus = :inventoryStatus AND c.isDeleted = :deleted " +
      "GROUP BY c.componentType, c.donation.bloodAbo, c.donation.bloodRh " +
      "ORDER BY c.componentType, c.donation.bloodAbo, c.donation.bloodRh";
  
  public static final String NAME_FIND_STOCK_LEVELS = "Component.findStockLevels";
  public static final String QUERY_FIND_STOCK_LEVELS =
      "SELECT NEW dto.StockLevelDTO(c.componentType, c.donation.bloodAbo, c.donation.bloodRh, COUNT(c)) " +
      "FROM Component c " +
      "WHERE c.inventoryStatus = :inventoryStatus AND c.isDeleted = :deleted " +
      "GROUP BY c.componentType, c.donation.bloodAbo, c.donation.bloodRh " +
      "ORDER BY c.componentType, c.donation.bloodAbo, c.donation.bloodRh";
}
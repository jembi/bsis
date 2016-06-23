package org.jembi.bsis.model.inventory;

public enum InventoryStatus {
  /**
   * The component has not been labelled yet. This is the initial status.
   */
  NOT_IN_STOCK,
  /**
   * The component has been labelled and is now in stock.
   */
  IN_STOCK,
  /**
   * The component was in stock but has subsequently been removed. It may have been issued or discarded.
   */
  REMOVED;
}

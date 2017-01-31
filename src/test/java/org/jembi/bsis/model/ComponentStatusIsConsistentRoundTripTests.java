package org.jembi.bsis.model;

import static org.jembi.bsis.helpers.builders.ComponentBuilder.aComponent;

import javax.validation.ConstraintViolationException;

import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

/**
 * ComponentStatusIsConsistentRoundTripTests validates all non compatible combinations (there's unit
 * tests for the compatible ones in ComponentStatusIsConsistentConstraintValidatorTests)
 */
public class ComponentStatusIsConsistentRoundTripTests extends ContextDependentTestSuite {
  
  @Test(expected = ConstraintViolationException.class)
  public void testPersistComponentInStockDiscarded_shouldThrow() {
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.DISCARDED).buildAndPersist(entityManager);
  }
  
  @Test(expected = ConstraintViolationException.class)
  public void testPersistComponentInStockProcessed_shouldThrow() {
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.PROCESSED).buildAndPersist(entityManager);
  }
  
  @Test(expected = ConstraintViolationException.class)
  public void testPersistComponentInStockQuarantined_shouldThrow() {
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.QUARANTINED).buildAndPersist(entityManager);
  }
  
  @Test(expected = ConstraintViolationException.class)
  public void testPersistComponentInStockIssued_shouldThrow() {
    aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.ISSUED).buildAndPersist(entityManager);
  }
  
  @Test(expected = ConstraintViolationException.class)
  public void testPersistComponentNotInStockIssued_shouldThrow() {
    aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.ISSUED).buildAndPersist(entityManager);
  }
  
  @Test(expected = ConstraintViolationException.class)
  public void testPersistComponentRemovedExpired_shouldThrow() {
    aComponent().withInventoryStatus(InventoryStatus.REMOVED).withStatus(ComponentStatus.EXPIRED).buildAndPersist(entityManager);
  }
  
  @Test(expected = ConstraintViolationException.class)
  public void testPersistComponentRemovedUnsafe_shouldThrow() {
    aComponent().withInventoryStatus(InventoryStatus.REMOVED).withStatus(ComponentStatus.UNSAFE).buildAndPersist(entityManager);
  }
  
  @Test(expected = ConstraintViolationException.class)
  public void testPersistComponentRemovedQuarantined_shouldThrow() {
    aComponent().withInventoryStatus(InventoryStatus.REMOVED).withStatus(ComponentStatus.QUARANTINED).buildAndPersist(entityManager);
  }
}

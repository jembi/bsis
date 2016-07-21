package org.jembi.bsis.constraintvalidator;

import org.jembi.bsis.helpers.builders.ComponentBuilder;
import org.jembi.bsis.model.component.Component;
import org.jembi.bsis.model.component.ComponentStatus;
import org.jembi.bsis.model.inventory.InventoryStatus;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * ComponentStatusConsistencyConstraintValidatorTests validates all compatible combinations
 * 
 * IN_STOCK_COMPATIBLE_STATUSES = AVAILABLE, EXPIRED, UNSAFE 
 * NOT_IN_STOCK_COMPATIBLE_STATUSES = AVAILABLE, EXPIRED, UNSAFE, DISCARDED, PROCESSED, QUARANTINED 
 * REMOVED_COMPATIBLE_STATUSES = ISSUED, DISCARDED
 * 
 */
public class ComponentStatusConsistencyConstraintValidatorTests extends ContextDependentTestSuite {
  
  @Autowired
  private ComponentStatusConsistencyConstraintValidator constraintValidator;

  @Test
  public void testConsistencyInStockAvailable_shouldBeCompatible() {
    Component component = ComponentBuilder.aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.AVAILABLE).build();
    boolean valid = constraintValidator.isValid(component, null);
    Assert.assertTrue("Is compatible", valid);
  }

  @Test
  public void testConsistencyInStockExpired_shouldBeCompatible() {
    Component component = ComponentBuilder.aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.EXPIRED).build();
    boolean valid = constraintValidator.isValid(component, null);
    Assert.assertTrue("Is compatible", valid);
  }

  @Test
  public void testConsistencyInStockUnsafe_shouldBeCompatible() {
    Component component = ComponentBuilder.aComponent().withInventoryStatus(InventoryStatus.IN_STOCK).withStatus(ComponentStatus.UNSAFE).build();
    boolean valid = constraintValidator.isValid(component, null);
    Assert.assertTrue("Is compatible", valid);
  }
  
  @Test
  public void testConsistencyNotInStockAvailable_shouldBeCompatible() {
    Component component = ComponentBuilder.aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.AVAILABLE).build();
    boolean valid = constraintValidator.isValid(component, null);
    Assert.assertTrue("Is compatible", valid);
  }

  @Test
  public void testConsistencyNotInStockExpired_shouldBeCompatible() {
    Component component = ComponentBuilder.aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.EXPIRED).build();
    boolean valid = constraintValidator.isValid(component, null);
    Assert.assertTrue("Is compatible", valid);
  }

  @Test
  public void testConsistencyNotInStockUnsafe_shouldBeCompatible() {
    Component component = ComponentBuilder.aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.UNSAFE).build();
    boolean valid = constraintValidator.isValid(component, null);
    Assert.assertTrue("Is compatible", valid);
  }
  
  @Test
  public void testConsistencyNotInStockDiscarded_shouldBeCompatible() {
    Component component = ComponentBuilder.aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.DISCARDED).build();
    boolean valid = constraintValidator.isValid(component, null);
    Assert.assertTrue("Is compatible", valid);
  }

  @Test
  public void testConsistencyNotInStockProcessed_shouldBeCompatible() {
    Component component = ComponentBuilder.aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.PROCESSED).build();
    boolean valid = constraintValidator.isValid(component, null);
    Assert.assertTrue("Is compatible", valid);
  }

  @Test
  public void testConsistencyNotInStockQuarantined_shouldBeCompatible() {
    Component component = ComponentBuilder.aComponent().withInventoryStatus(InventoryStatus.NOT_IN_STOCK).withStatus(ComponentStatus.QUARANTINED).build();
    boolean valid = constraintValidator.isValid(component, null);
    Assert.assertTrue("Is compatible", valid);
  }
  
  @Test
  public void testConsistencyRemovedIssued_shouldBeCompatible() {
    Component component = ComponentBuilder.aComponent().withInventoryStatus(InventoryStatus.REMOVED).withStatus(ComponentStatus.ISSUED).build();
    boolean valid = constraintValidator.isValid(component, null);
    Assert.assertTrue("Is compatible", valid);
  }

  @Test
  public void testConsistencyRemovedDiscarded_shouldBeCompatible() {
    Component component = ComponentBuilder.aComponent().withInventoryStatus(InventoryStatus.REMOVED).withStatus(ComponentStatus.DISCARDED).build();
    boolean valid = constraintValidator.isValid(component, null);
    Assert.assertTrue("Is compatible", valid);
  }

}

package org.jembi.bsis.model;

import static org.jembi.bsis.helpers.builders.OrderFormItemBuilder.anOrderItemForm;

import javax.persistence.PersistenceException;

import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

public class OrderFormItemRoundTripTests extends ContextDependentTestSuite {
  
  @Test
  public void testPersistItemWithValidItem() {
    anOrderItemForm().buildAndPersist(entityManager);
  }
  
  @Test(expected = PersistenceException.class)
  public void testPersistItemWithNoBloodAbo_shouldThrow() {
    anOrderItemForm().withBloodAbo(null).buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistItemWithNoBloodRh_shouldThrow() {
    anOrderItemForm().withBloodRh(null).buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistItemWithNoOrderForm_shouldThrow() {
    anOrderItemForm().withOrderForm(null).buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistItemWithNoComponentType_shouldThrow() {
    anOrderItemForm().withComponentType(null).buildAndPersist(entityManager);
  }

}

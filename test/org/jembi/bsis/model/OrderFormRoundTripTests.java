package org.jembi.bsis.model;

import static org.jembi.bsis.helpers.builders.OrderFormBuilder.anOrderForm;

import javax.persistence.PersistenceException;

import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

public class OrderFormRoundTripTests extends ContextDependentTestSuite {
  
  @Test
  public void testPersistOrderFormWithValidOrderForm() {
    anOrderForm().buildAndPersist(entityManager);
  }
  
  @Test(expected = PersistenceException.class)
  public void testPersistOrderFormWithNoOrderDate_shouldThrow() {
    anOrderForm().withOrderDate(null).buildAndPersist(entityManager);
  }
  
  @Test(expected = PersistenceException.class)
  public void testPersistOrderFormWithNoDispatchedFrom_shouldThrow() {
    anOrderForm().withDispatchedFrom(null).buildAndPersist(entityManager);
  }
  
  @Test(expected = PersistenceException.class)
  public void testPersistOrderFormWithNoDispatchedTo_shouldThrow() {
    anOrderForm().withDispatchedTo(null).buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistOrderFormWithNoType_shouldThrow() {
    anOrderForm().withOrderType(null).buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistOrderFormWithNoStatus_shouldThrow() {
    anOrderForm().withOrderStatus(null).buildAndPersist(entityManager);
  }

}

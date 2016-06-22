package org.jembi.bsis.model;

import static org.jembi.bsis.helpers.builders.ReturnFormBuilder.aReturnForm;

import javax.persistence.PersistenceException;

import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

public class ReturnFormRoundTripTests extends ContextDependentTestSuite {
  
  @Test
  public void testPersistReturnFormWithValidReturnForm() {
    aReturnForm().buildAndPersist(entityManager);
  }
  
  @Test(expected = PersistenceException.class)
  public void testPersistReturnFormWithNoReturnDate_shouldThrow() {
    aReturnForm().withReturnDate(null).buildAndPersist(entityManager);
  }
  
  @Test(expected = PersistenceException.class)
  public void testPersistReturnFormWithNoDispatchedFrom_shouldThrow() {
    aReturnForm().withReturnedFrom(null).buildAndPersist(entityManager);
  }
  
  @Test(expected = PersistenceException.class)
  public void testPersistReturnFormWithNoDispatchedTo_shouldThrow() {
    aReturnForm().withReturnedTo(null).buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistReturnFormWithNoStatus_shouldThrow() {
    aReturnForm().withReturnStatus(null).buildAndPersist(entityManager);
  }

}

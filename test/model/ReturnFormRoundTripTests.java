package model;

import static helpers.builders.ReturnFormBuilder.aReturnForm;

import javax.persistence.PersistenceException;

import org.junit.Test;

import suites.ContextDependentTestSuite;

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

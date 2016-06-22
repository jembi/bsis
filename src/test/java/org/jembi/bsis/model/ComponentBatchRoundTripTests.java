package org.jembi.bsis.model;

import static org.jembi.bsis.helpers.builders.ComponentBatchBuilder.aComponentBatch;

import javax.persistence.PersistenceException;

import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

public class ComponentBatchRoundTripTests extends ContextDependentTestSuite {
  
  @Test
  public void testPersistComponentBatchWithValidComponentBatch() {
    aComponentBatch().buildAndPersist(entityManager);
  }
  
  @Test(expected = PersistenceException.class)
  public void testPersistComponentBatchWithNoDeliveryDate_shouldThrow() {
    aComponentBatch().withDeliveryDate(null).buildAndPersist(entityManager);
  }
  
  @Test(expected = PersistenceException.class)
  public void testPersistComponentBatchWithNoCollectionDate_shouldThrow() {
    aComponentBatch().withCollectionDate(null).buildAndPersist(entityManager);
  }
  
  @Test(expected = PersistenceException.class)
  public void testPersistComponentBatchWithNoStatus_shouldThrow() {
    aComponentBatch().withStatus(null).buildAndPersist(entityManager);
  }
  
  @Test(expected = PersistenceException.class)
  public void testPersistComponentBatchWithNoLocation_shouldThrow() {
    aComponentBatch().withLocation(null).buildAndPersist(entityManager);
  }

}

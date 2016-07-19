package org.jembi.bsis.helpers.persisters;

import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aLocationPersister;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.testbatch.TestBatch;

public class TestBatchPersister extends AbstractEntityPersister<TestBatch> {

  @Override
  public TestBatch deepPersist(TestBatch testBatch, EntityManager entityManager) {
    if (testBatch.getLocation() != null) {
      aLocationPersister().deepPersist(testBatch.getLocation(), entityManager);
    }
    return persist(testBatch, entityManager);
  }
}

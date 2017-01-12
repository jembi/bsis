package org.jembi.bsis.repository;

import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBuilder.aComponentTypeCombination;

import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

public class ComponentTypeCombinationRoundTripTests extends ContextDependentTestSuite {

  @Test(expected = javax.persistence.PersistenceException.class)
  public void testCombinationNameIsUnique_shouldThrow() {
    aComponentTypeCombination().withCombinationName("combination").buildAndPersist(entityManager);
    aComponentTypeCombination().withCombinationName("combination").buildAndPersist(entityManager);
  }
  
  @Test(expected = javax.persistence.PersistenceException.class)
  public void testCombinationNameIsNotNull_shouldThrow() {
    aComponentTypeCombination().withCombinationName(null).buildAndPersist(entityManager);
  }
}

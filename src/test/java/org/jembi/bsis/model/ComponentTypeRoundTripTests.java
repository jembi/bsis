package org.jembi.bsis.model;

import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;

import javax.persistence.PersistenceException;

import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

public class ComponentTypeRoundTripTests extends ContextDependentTestSuite {

  @Test
  public void testPersistValidComponentType_shouldSave() {
    aComponentType().buildAndPersist(entityManager);
  }
  
  @Test(expected = PersistenceException.class)
  public void testPersistComponentTypeWithExistingName_shouldThrow() {
    aComponentType().withComponentTypeName("x").buildAndPersist(entityManager);
    aComponentType().withComponentTypeName("x").buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistComponentTypeWithExistingCode_shouldThrow() {
    aComponentType().withComponentTypeCode("x").buildAndPersist(entityManager);
    aComponentType().withComponentTypeCode("x").buildAndPersist(entityManager);
  }

}
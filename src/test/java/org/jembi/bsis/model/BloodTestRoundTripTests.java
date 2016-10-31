package org.jembi.bsis.model;

import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;

import javax.persistence.PersistenceException;

import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

public class BloodTestRoundTripTests extends ContextDependentTestSuite {

  @Test
  public void testPersistBloodTest() {
    aBloodTest().withTestName("test").withTestNameShort("t1").buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistBloodTestWithNoTestName_shouldThrow() {
    aBloodTest().withTestNameShort("t1").buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistBloodTestWithNoTestNameShort_shouldThrow() {
    aBloodTest().withTestName("test").buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistBloodTestWithNotUniqueTestName_shouldThrow() {
    aBloodTest().withTestName("test").withTestNameShort("t1").buildAndPersist(entityManager);
    aBloodTest().withTestName("test").withTestNameShort("t2").buildAndPersist(entityManager);
  }
}
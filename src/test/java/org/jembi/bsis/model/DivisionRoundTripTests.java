package org.jembi.bsis.model;

import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

public class DivisionRoundTripTests extends ContextDependentTestSuite {

  @Test
  public void testPersistValidDivision() {
    aDivision().buildAndPersist(entityManager);
  }

  @Test
  public void testPersistValidDivisionWithParent() {
    aDivision().withLevel(2).withParent(aDivision().withName("default.division.name2").withLevel(1).build())
        .buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistDivisionWithSameName_shouldThrow() {
    aDivision().withName("sameName").buildAndPersist(entityManager);
    aDivision().withName("sameName").buildAndPersist(entityManager);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testPersistDivisionWithNoName_shouldThrow() {
    aDivision().withName(null).buildAndPersist(entityManager);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testPersistDivisionWithBlankName_shouldThrow() {
    aDivision().withName("").buildAndPersist(entityManager);
  }

}

package org.jembi.bsis.model;

import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;

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
    aDivision().withLevel(2).withParent(aDivision().withLevel(1).build()).buildAndPersist(entityManager);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testPersistValidDivisionWithNoName_shouldThrow() {
    aDivision().withName(null).buildAndPersist(entityManager);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testPersistValidDivisionWithBlankName_shouldThrow() {
    aDivision().withName("").buildAndPersist(entityManager);
  }

}

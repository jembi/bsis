package org.jembi.bsis.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

public class DivisionRoundTripTests extends ContextDependentTestSuite {

  @Test
  public void testPersistValidDivision() {
    aDivision().buildAndPersist(entityManager);
  }

  @Test
  public void testPersistUTF8Division() {
    String russian = "До свидания"; // means bye
    Division division = aDivision().withName(russian).buildAndPersist(entityManager);
    Division savedDivision = entityManager.find(Division.class, division.getId());
    // ensures that it's possible to store and retrieve UTF-8 characters from a HSQL database
    assertThat(savedDivision.getName(), is(russian));
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

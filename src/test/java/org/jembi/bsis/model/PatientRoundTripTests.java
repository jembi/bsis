package org.jembi.bsis.model;

import static org.jembi.bsis.helpers.builders.PatientBuilder.aPatient;

import javax.validation.ConstraintViolationException;

import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

public class PatientRoundTripTests extends ContextDependentTestSuite {

  @Test
  public void testPersistWithName1AndName2_shouldSave() {
    aPatient()
      .withName1("name1")
      .withName2("name2")
      .buildAndPersist(entityManager);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testPersistWithoutName1_shouldThrow() {
    aPatient()
      .withName1(null)
      .withName2("name2")
      .buildAndPersist(entityManager);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testPersistWithoutName2_shouldThrow() {
    aPatient()
      .withName1("name1")
      .withName2(null)
      .buildAndPersist(entityManager);
  }

}

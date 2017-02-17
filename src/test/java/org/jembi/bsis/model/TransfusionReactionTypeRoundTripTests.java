package org.jembi.bsis.model;

import static org.jembi.bsis.helpers.builders.TransfusionReactionTypeBuilder.aTransfusionReactionType;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

public class TransfusionReactionTypeRoundTripTests extends ContextDependentTestSuite {

  @Test
  public void testPersistValidTransfusionReactionType() {
    aTransfusionReactionType().buildAndPersist(entityManager);
  }

  @Test
  public void testPersistValidTransfusionReactionTypeWithNoDescription_shouldPersistSuccessfully() {
    aTransfusionReactionType().withDescription(null).buildAndPersist(entityManager);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testPersistTransfusionReactionTypeWithNoName_shouldThrow() {
    aTransfusionReactionType().withName(null).buildAndPersist(entityManager);
  }

  @Test(expected = ConstraintViolationException.class)
  public void testPersistTransfusionReactionTypeBlankName_shouldThrow() {
    aTransfusionReactionType().withName("").buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistTransfusionReactionTypeWithNonUniqueName_shouldThrow() {
    aTransfusionReactionType().withName("Same").buildAndPersist(entityManager);
    aTransfusionReactionType().withName("Same").buildAndPersist(entityManager);
  }

  @Test(expected = PersistenceException.class)
  public void testPersistTransfusionReactionTypeTooLongName_shouldThrow() {
    aTransfusionReactionType()
        .withName("A very long transfusion reaction type name. One that is longer than 255 characters "
            + " which is the maximum length of the transfusion reaction type. This is indeed a very long"
            + " name, one that contains whitespace, full stops and useless information, except for the"
            + " fact that it's useful so that it may exceed the maximum length of 255 characters")
      .buildAndPersist(entityManager);
  }
}

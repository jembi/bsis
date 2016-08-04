package org.jembi.bsis.repository;

import static org.jembi.bsis.helpers.builders.ComponentStatusChangeReasonBuilder.aComponentStatusChangeReason;

import javax.persistence.NoResultException;

import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReason;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonCategory;
import org.jembi.bsis.model.componentmovement.ComponentStatusChangeReasonType;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ComponentStatusChangeReasonRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  ComponentStatusChangeReasonRepository componentStatusChangeReasonRepository;
  
  @Test
  public void testFindReasonByCategoryAndType_returnsRightReason() {
    // Set up
    ComponentStatusChangeReason reason1 = aComponentStatusChangeReason()
        .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.UNSAFE)
        .withComponentStatusChangeReasonType(ComponentStatusChangeReasonType.UNSAFE_PARENT)
        .buildAndPersist(entityManager);
    aComponentStatusChangeReason()
        .withComponentStatusChangeReasonCategory(ComponentStatusChangeReasonCategory.DISCARDED)
        .buildAndPersist(entityManager);

    // Run test
    ComponentStatusChangeReason returnedReason = componentStatusChangeReasonRepository.findComponentStatusChangeReasonByCategoryAndType(
            ComponentStatusChangeReasonCategory.UNSAFE, ComponentStatusChangeReasonType.UNSAFE_PARENT);

    // Verify
    Assert.assertNotNull("A reason is returned", returnedReason);
    Assert.assertEquals("The correct reason is returned", reason1, returnedReason);
    Assert.assertEquals("The correct reason is returned", reason1.getCategory(), returnedReason.getCategory());
    Assert.assertEquals("The correct reason is returned", reason1.getType(), returnedReason.getType());
  }

  @Test(expected = NoResultException.class)
  public void testFindNonExistentReasonByCategoryAndType_shouldThrow() {
    // Run test
    componentStatusChangeReasonRepository.findComponentStatusChangeReasonByCategoryAndType(
        ComponentStatusChangeReasonCategory.UNSAFE, ComponentStatusChangeReasonType.UNSAFE_PARENT);
  }
}

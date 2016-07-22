package org.jembi.bsis.repository;

import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ComponentTypeRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private ComponentTypeRepository componentTypeRepository;

  @Test
  public void testGetComponentTypesThatCanBeIssued_shouldReturnComponentsTypesThatCanBeIssued() {

    ComponentType componentType1 = aComponentType().withCanBeIssued(true).buildAndPersist(entityManager);
    ComponentType componentType2 = aComponentType().withCanBeIssued(true).buildAndPersist(entityManager);
    aComponentType().withCanBeIssued(false).buildAndPersist(entityManager);

    List<ComponentType> all = componentTypeRepository.getAllComponentTypesThatCanBeIssued();
    assertEquals("There are 2 ComponentTypes", 2, all.size());
    assertTrue("componentType1 is returned", all.contains(componentType1));
    assertTrue("componentType2 is returned", all.contains(componentType2));
  }

  @Test
  public void testGetComponentTypesThatCanBeIssued_shouldntReturnDeletedComponentTypes() {

    ComponentType componentType = aComponentType().withCanBeIssued(true).buildAndPersist(entityManager);
    aComponentType().withCanBeIssued(true).thatIsDeleted().buildAndPersist(entityManager);

    List<ComponentType> all = componentTypeRepository.getAllComponentTypesThatCanBeIssued();
    assertEquals("There is 1 ComponentType", 1, all.size());
    assertTrue("componentType is returned", all.contains(componentType));
  }
}

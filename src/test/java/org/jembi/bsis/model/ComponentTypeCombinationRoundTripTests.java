package org.jembi.bsis.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBuilder.aComponentTypeCombination;

import java.util.Arrays;
import java.util.HashSet;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;

public class ComponentTypeCombinationRoundTripTests extends ContextDependentTestSuite {

  @Test
  public void testPersistWithDefaultsFromBuilder_shouldSave() {
    aComponentType().buildAndPersist(entityManager);
  }
  
  @Test
  public void testDeletedComponentTypeCombinations_shouldNotReturnDeletedProducedComponentTypeCombinations() {
    // Set up data
    ComponentType sourceComponentType = aComponentType().buildAndPersist(entityManager);

    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination()
        .withCombinationName("Whole Blood")
        .withComponentTypes(Arrays.asList(aComponentType().buildAndPersist(entityManager)))
        .withSourceComponents(new HashSet<>(Arrays.asList(sourceComponentType)))
        .buildAndPersist(entityManager);
    
    ComponentTypeCombination deletedComponentTypeCombinations = aComponentTypeCombination()
        .withCombinationName("Old Blood")
        .withComponentTypes(Arrays.asList(aComponentType().buildAndPersist(entityManager)))
        .withSourceComponents(new HashSet<>(Arrays.asList(sourceComponentType)))
        .thatIsDeleted()
        .buildAndPersist(entityManager);

    sourceComponentType.setProducedComponentTypeCombinations(new HashSet<>(Arrays.asList(componentTypeCombination, deletedComponentTypeCombinations)));
    entityManager.persist(sourceComponentType);
    entityManager.flush();

    // clear hibernate cache
    entityManager.clear();

    ComponentType persistedComponentType = entityManager.createQuery("SELECT ct FROM ComponentType ct WHERE ct.id = :id", ComponentType.class)
        .setParameter("id", sourceComponentType.getId())
        .getSingleResult();

    // Asserts
    assertThat("Only one component type combination is returned", persistedComponentType.getProducedComponentTypeCombinations().size(), is(1));
    assertThat("Correct component type combination is returned",
        persistedComponentType.getProducedComponentTypeCombinations().iterator().next().getId(), is(componentTypeCombination.getId()));
  }

  @Test
  public void testDeletedComponentTypeCombinations_shouldNotReturnDeletedSourceComponentTypeCombinations() {
    // Set up data
    ComponentType producedComponentType = aComponentType().buildAndPersist(entityManager);

    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination()
        .withCombinationName("Whole Blood")
        .withComponentTypes(Arrays.asList(producedComponentType))
        .withSourceComponents(new HashSet<>(Arrays.asList(aComponentType().buildAndPersist(entityManager))))
        .buildAndPersist(entityManager);
    
    ComponentTypeCombination deletedComponentTypeCombinations = aComponentTypeCombination()
        .withCombinationName("Old Blood")
        .withComponentTypes(Arrays.asList(producedComponentType))
        .withSourceComponents(new HashSet<>(Arrays.asList(aComponentType().buildAndPersist(entityManager))))
        .thatIsDeleted()
        .buildAndPersist(entityManager);

    producedComponentType.setComponentTypeCombinations((Arrays.asList(componentTypeCombination, deletedComponentTypeCombinations)));
    entityManager.persist(producedComponentType);
    entityManager.flush();

    // clear hibernate cache
    entityManager.clear();

    ComponentType persistedComponentType = entityManager.createQuery("SELECT ct FROM ComponentType ct WHERE ct.id = :id", 
        ComponentType.class)
        .setParameter("id", producedComponentType.getId())
        .getSingleResult();

    // Asserts
    assertThat("Only one component type combination is returned", persistedComponentType.getComponentTypeCombinations().size(), is(1));
    assertThat("Correct component type combination is returned",
        persistedComponentType.getComponentTypeCombinations().iterator().next().getId(), is(componentTypeCombination.getId()));
  }
}
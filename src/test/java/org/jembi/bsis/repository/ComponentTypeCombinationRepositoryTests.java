package org.jembi.bsis.repository;

import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBuilder.aComponentTypeCombination;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.jembi.bsis.helpers.builders.ComponentTypeBuilder;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ComponentTypeCombinationRepositoryTests extends SecurityContextDependentTestSuite {

  @Autowired
  private ComponentTypeCombinationRepository componentTypeCombinationRepository;
    
  @Test
  public void testGetAllComponentTypeCombinationsIncludingDeleted_shouldReturnAllEntities() { 
    ComponentTypeCombination deletedComponentTypeCombination = aComponentTypeCombination()
        .withCombinationName("123")
        .thatIsDeleted()
        .buildAndPersist(entityManager);
    
    ComponentTypeCombination nonDeletedComponentTypeCombination = aComponentTypeCombination()
        .buildAndPersist(entityManager);
    
    List<ComponentTypeCombination> returnedComponentTypeCombinations = componentTypeCombinationRepository.getAllComponentTypeCombinations(true);

    assertThat(returnedComponentTypeCombinations.size(), is(2));
    assertTrue(returnedComponentTypeCombinations.contains(deletedComponentTypeCombination));
    assertTrue(returnedComponentTypeCombinations.contains(nonDeletedComponentTypeCombination));
  }
  
  @Test
  public void testGetAllComponentTypeCombinationsNotIncludingDeleted_shouldReturnNotDeletedEntities() {
    ComponentTypeCombination nonDeletedComponentTypeCombination = aComponentTypeCombination()
        .withCombinationName("123")
        .buildAndPersist(entityManager);
    
    ComponentTypeCombination deletedComponentTypeCombination = aComponentTypeCombination()
        .thatIsDeleted()
        .buildAndPersist(entityManager);
    
    List<ComponentTypeCombination> returnedComponentTypeCombinations = componentTypeCombinationRepository.getAllComponentTypeCombinations(false);

    assertThat(returnedComponentTypeCombinations.size(), is(1));
    assertTrue(returnedComponentTypeCombinations.contains(nonDeletedComponentTypeCombination));
    assertFalse(returnedComponentTypeCombinations.contains(deletedComponentTypeCombination));
  }
  
  @Test
  public void testSaveComponentTypeCombination_shouldPersistTrackingFieldsCorrectly() {
    // Set up data
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination()
        .withCombinationName("combination")
        .withComponentType(ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager))
        .withSourceComponentType(ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager))
        .build();

    // Run test
    componentTypeCombinationRepository.save(componentTypeCombination);

    // Verify
    Assert.assertNotNull("createdDate has been populated", componentTypeCombination.getCreatedDate());
    Assert.assertNotNull("createdBy has been populated", componentTypeCombination.getCreatedBy());
    Assert.assertNotNull("lastUpdated has been populated", componentTypeCombination.getLastUpdated());
    Assert.assertNotNull("lastUpdatedBy has been populated", componentTypeCombination.getLastUpdatedBy());
  }

  @Test
  public void testUpdateComponentTypeCombination_shouldPersistTrackingFieldsCorrectly() {
    // Set up data
    ComponentTypeCombination componentTypeCombination = aComponentTypeCombination()
        .withCombinationName("combination")
        .withComponentType(ComponentTypeBuilder.aComponentType().build())
        .withSourceComponentType(ComponentTypeBuilder.aComponentType().build())
        .buildAndPersist(entityManager);

    // Run test
    componentTypeCombinationRepository.update(componentTypeCombination);
    
    // Verify
    Assert.assertNotNull("createdDate has been populated", componentTypeCombination.getCreatedDate());
    Assert.assertNotNull("createdBy has been populated", componentTypeCombination.getCreatedBy());
    Assert.assertNotNull("lastUpdated has been populated", componentTypeCombination.getLastUpdated());
    Assert.assertNotNull("lastUpdatedBy has been populated", componentTypeCombination.getLastUpdatedBy());
  }
}
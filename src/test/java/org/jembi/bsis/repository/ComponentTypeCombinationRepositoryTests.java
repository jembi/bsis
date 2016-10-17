package org.jembi.bsis.repository;

import java.util.List;

import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBuilder.aComponentTypeCombination;
import static org.junit.Assert.assertTrue;

import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ComponentTypeCombinationRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private ComponentTypeCombinationRepository componentTypeCombinationRepository;
    
  @Test
  public void testGetAllComponentTypeCombinationsIncludingDeleted_shouldReturnAllEntities() { 
    ComponentTypeCombination deletedComponentTypeCombination = aComponentTypeCombination()
        .withCombinationName("123")
        .thatIsDeleted()
        .buildAndPersist(entityManager); 
    
    List<ComponentTypeCombination> returnedComponentTypeCombinations = componentTypeCombinationRepository.getAllComponentTypeCombinations(true);

    assertTrue(returnedComponentTypeCombinations.contains(deletedComponentTypeCombination));
  }
  
  @Test
  public void testGetAllComponentTypeCombinationsNotIncludingDeleted_shouldReturnNotDeletedEntities() {
    ComponentTypeCombination nonDeletedComponentTypeCombination = aComponentTypeCombination()
        .withCombinationName("123")
        .buildAndPersist(entityManager);

    List<ComponentTypeCombination> returnedComponentTypeCombinations = componentTypeCombinationRepository.getAllComponentTypeCombinations(false);

    assertTrue(returnedComponentTypeCombinations.contains(nonDeletedComponentTypeCombination));
  }
}
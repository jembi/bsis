package org.jembi.bsis.repository;

import java.util.List;

import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBuilder.aComponentTypeCombination;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
        .buildAndPersist(entityManager);
    
    ComponentTypeCombination deletedComponentTypeCombination = aComponentTypeCombination()
        .thatIsDeleted()
        .buildAndPersist(entityManager);
    
    List<ComponentTypeCombination> returnedComponentTypeCombinations = componentTypeCombinationRepository.getAllComponentTypeCombinations(false);

    assertThat(returnedComponentTypeCombinations.size(), is(1));
    assertTrue(returnedComponentTypeCombinations.contains(nonDeletedComponentTypeCombination));
    assertFalse(returnedComponentTypeCombinations.contains(deletedComponentTypeCombination));
  }
}
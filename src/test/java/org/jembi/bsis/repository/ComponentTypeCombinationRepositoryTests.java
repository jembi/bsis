package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBuilder.aComponentTypeCombination;

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

    ComponentTypeCombination expectedComponentTypeCombinations = aComponentTypeCombination()
        .withId(deletedComponentTypeCombination.getId())
        .build();

    List<ComponentTypeCombination> returnedComponentTypeCombinations = componentTypeCombinationRepository.getAllComponentTypeCombinations(deletedComponentTypeCombination.getIsDeleted());

    assertThat(returnedComponentTypeCombinations.size(), is(1));
    assertThat(returnedComponentTypeCombinations.get(0), is(expectedComponentTypeCombinations));
  }
  
  @Test
  public void testGetAllComponentTypeCombinationsNotIncludingDeleted_shouldReturnNotDeletedEntities() {
    ComponentTypeCombination notDeletedComponentTypeCombination = aComponentTypeCombination()
        .buildAndPersist(entityManager);

    ComponentTypeCombination expectedComponentTypeCombinations = aComponentTypeCombination()
        .withId(notDeletedComponentTypeCombination.getId())
        .build();

    List<ComponentTypeCombination> returnedComponentTypeCombinations = componentTypeCombinationRepository.getAllComponentTypeCombinations(notDeletedComponentTypeCombination.getIsDeleted());

    assertThat(returnedComponentTypeCombinations.size(), is(1));
    assertThat(returnedComponentTypeCombinations.get(0), is(expectedComponentTypeCombinations));
  }
}
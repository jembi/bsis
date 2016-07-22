package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ComponentTypeRepositoryTests extends ContextDependentTestSuite {
  
  @Autowired
  private ComponentTypeRepository componentTypeRepository;
  
  @Test
  public void testFindComponentTypeByCodeWithMultipleComponentTypes_shouldReturnCorrectComponentType() {
    String componentTypeCode = "0011";
    
    // Expected
    ComponentType expectedComponentType = aComponentType()
        .withComponentTypeCode(componentTypeCode)
        .buildAndPersist(entityManager);
    
    // Excluded by code
    aComponentType().withComponentTypeCode("2012").buildAndPersist(entityManager);
    
    ComponentType returnedComponentType = componentTypeRepository.findComponentTypeByCode(componentTypeCode);
    
    assertThat(returnedComponentType, is(expectedComponentType));
  }

  @Test
  public void testFindComponentTypeByCodeWithDeletedComponentType_shouldReturnComponentType() {
    String componentTypeCode = "0011";

    ComponentType expectedComponentType = aComponentType()
        .withComponentTypeCode(componentTypeCode)
        .thatIsDeleted()
        .buildAndPersist(entityManager);

    ComponentType returnedComponentType = componentTypeRepository.findComponentTypeByCode(componentTypeCode);
    
    assertThat(returnedComponentType, is(expectedComponentType));
  }

}

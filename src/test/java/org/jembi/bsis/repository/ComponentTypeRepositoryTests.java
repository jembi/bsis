package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;

import javax.persistence.NoResultException;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ComponentTypeRepositoryTests extends ContextDependentTestSuite {
  
  @Autowired
  private ComponentTypeRepository componentTypeRepository;
  
  @Test
  public void testFindComponentByCode_shouldReturnCorrectComponent() {
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
  
  @Test(expected = NoResultException.class)
  public void testFindComponentByCodeWithDeletedComponent_shouldThrowNoResultException() {
    String componentTypeCode = "0011";
    
     aComponentType().withComponentTypeCode(componentTypeCode).thatIsDeleted().buildAndPersist(entityManager);
    
    componentTypeRepository.findComponentTypeByCode(componentTypeCode);
  }

}

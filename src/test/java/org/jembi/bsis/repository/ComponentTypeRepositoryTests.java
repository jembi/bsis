package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
  public void testIsUniqueComponentTypeName_shouldReturnFalseIfComponentNameIsNotUnique() {
    String componentTypeCode = "0011";
    String componentTypeName = "Blood";
    
    // Test data: ComponentType with same name
    aComponentType().withComponentTypeCode(componentTypeCode).withComponentTypeName(componentTypeName).buildAndPersist(entityManager);
    
    // Run test
    Boolean unique = componentTypeRepository.isUniqueComponentTypeName(2L, componentTypeName);
    
    // Verify result
    assertThat(unique, is(Boolean.FALSE));
  }
  
  @Test
  public void testIsUniqueComponentTypeName_shouldReturnTrueIfComponentTypeAlreadyExists() {
    String componentTypeCode = "0011";
    String componentTypeName = "Blood";
    
    // Test data: ComponentType already exists
    ComponentType componentType = aComponentType().withComponentTypeCode(componentTypeCode).withComponentTypeName(componentTypeName).buildAndPersist(entityManager);
    
    // Run test
    Boolean unique = componentTypeRepository.isUniqueComponentTypeName(componentType.getId(), componentTypeName);
    
    // Verify result
    assertThat(unique, is(Boolean.TRUE));
  }
  
  @Test
  public void testIsUniqueComponentTypeName_shouldReturnTrueForNewComponentType() {
    String componentTypeCode = "0011";
    String componentTypeName = "Blood";
    
    // Test data: ComponentType with different name
    aComponentType().withComponentTypeCode(componentTypeCode).withComponentTypeName(componentTypeName).buildAndPersist(entityManager);
    
    // Run test
    Boolean unique = componentTypeRepository.isUniqueComponentTypeName(null, "More Blood");
    
    // Verify result
    assertThat(unique, is(Boolean.TRUE));
  }
  
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

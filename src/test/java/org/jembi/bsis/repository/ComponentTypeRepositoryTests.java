package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentTypeBuilder.aComponentType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

import org.jembi.bsis.model.componenttype.ComponentType;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ComponentTypeRepositoryTests extends SecurityContextDependentTestSuite {
  
  @Autowired
  private ComponentTypeRepository componentTypeRepository;
  
  @Test
  public void testIsUniqueComponentTypeName_shouldReturnFalseIfComponentNameExists() {
    String componentTypeCode = "0011";
    String componentTypeName = "Blood";
    
    // Test data: ComponentType with same name
    aComponentType().withComponentTypeCode(componentTypeCode).withComponentTypeName(componentTypeName).buildAndPersist(entityManager);
    
    // Run test
    boolean unique = componentTypeRepository.isUniqueComponentTypeName(UUID.randomUUID(), componentTypeName);
    
    // Verify result
    assertThat(unique, is(false));
  }
  
  @Test
  public void testIsUniqueComponentTypeName_shouldReturnTrueIfComponentTypeAlreadyExists() {
    String componentTypeCode = "0011";
    String componentTypeName = "Blood";
    
    // Test data: ComponentType already exists
    ComponentType componentType = aComponentType().withComponentTypeCode(componentTypeCode).withComponentTypeName(componentTypeName).buildAndPersist(entityManager);
    
    // Run test
    boolean unique = componentTypeRepository.isUniqueComponentTypeName(componentType.getId(), componentTypeName);
    
    // Verify result
    assertThat(unique, is(true));
  }
  
  @Test
  public void testIsUniqueComponentTypeName_shouldReturnTrueForNewComponentType() {
    String componentTypeCode = "0011";
    String componentTypeName = "Blood";
    
    // Test data: ComponentType with different name
    aComponentType().withComponentTypeCode(componentTypeCode).withComponentTypeName(componentTypeName).buildAndPersist(entityManager);
    
    // Run test
    boolean unique = componentTypeRepository.isUniqueComponentTypeName(null, "More Blood");
    
    // Verify result
    assertThat(unique, is(true));
  }
  
  @Test
  public void testIsUniqueComponentTypeName_shouldReturnFalseIfComponentNameExistsButDeleted() {
    String componentTypeCode = "0011";
    String componentTypeName = "Blood";
    
    // Test data: ComponentType with same name but deleted
    aComponentType()
        .withComponentTypeCode(componentTypeCode)
        .withComponentTypeName(componentTypeName)
        .thatIsDeleted()
        .buildAndPersist(entityManager);
    
    // Run test
    boolean unique = componentTypeRepository.isUniqueComponentTypeName(null, componentTypeName);
    
    // Verify result
    assertThat(unique, is(false));
  }
  
  @Test
  public void testIsUniqueComponentTypeName_shouldReturnFalseIfComponentNameExistsButDifferentCase() {
    String componentTypeCode = "0011";
    String componentTypeName = "blood";
    
    // Test data: ComponentType with same name but different case
    aComponentType()
        .withComponentTypeCode(componentTypeCode)
        .withComponentTypeName(componentTypeName.toUpperCase())
        .buildAndPersist(entityManager);
    
    // Run test
    boolean unique = componentTypeRepository.isUniqueComponentTypeName(null, componentTypeName);
    
    // Verify result
    assertThat(unique, is(false));
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

    ComponentType componentType1 = aComponentType().thatCanBeIssued().buildAndPersist(entityManager);
    ComponentType componentType2 = aComponentType().thatCanBeIssued().buildAndPersist(entityManager);
    aComponentType().thatCanNotBeIssued().buildAndPersist(entityManager);

    List<ComponentType> all = componentTypeRepository.getAllComponentTypesThatCanBeIssued();
    assertEquals("There are 2 ComponentTypes", 2, all.size());
    assertTrue("componentType1 is returned", all.contains(componentType1));
    assertTrue("componentType2 is returned", all.contains(componentType2));
  }

  @Test
  public void testGetComponentTypesThatCanBeIssued_shouldntReturnDeletedComponentTypes() {

    ComponentType componentType = aComponentType().thatCanBeIssued().buildAndPersist(entityManager);
    aComponentType().thatCanBeIssued().thatIsDeleted().buildAndPersist(entityManager);

    List<ComponentType> all = componentTypeRepository.getAllComponentTypesThatCanBeIssued();
    assertEquals("There is 1 ComponentType", 1, all.size());
    assertTrue("componentType is returned", all.contains(componentType));
  }
  
  @Test
  public void testSaveComponentType_shouldPersistTrackingFieldsCorrectly() {

    ComponentType componentType = aComponentType().buildAndPersist(entityManager);

    componentTypeRepository.saveComponentType(componentType);
    
    // Verify
    assertNotNull("createdDate has been populated", componentType.getCreatedDate());
    assertNotNull("createdBy has been populated", componentType.getCreatedBy());
    assertNotNull("lastUpdated has been populated", componentType.getLastUpdated());
    assertNotNull("lastUpdatedBy has been populated", componentType.getLastUpdatedBy());
  }
}

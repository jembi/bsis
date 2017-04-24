package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.ComponentTypeCombinationBuilder.aComponentTypeCombination;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.UUID;

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
        .thatIsDeleted()
        .buildAndPersist(entityManager);
    
    ComponentTypeCombination nonDeletedComponentTypeCombination = aComponentTypeCombination()
        .withCombinationName("otherCombination")
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
        .withCombinationName("otherCombination")
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
        .withComponentType(ComponentTypeBuilder.aComponentType().build())
        .withSourceComponentType(ComponentTypeBuilder.aComponentType().buildAndPersist(entityManager))
        .buildAndPersist(entityManager);

    // Run test
    componentTypeCombinationRepository.update(componentTypeCombination);
    
    // Verify
    Assert.assertNotNull("createdDate has been populated", componentTypeCombination.getCreatedDate());
    Assert.assertNotNull("createdBy has been populated", componentTypeCombination.getCreatedBy());
    Assert.assertNotNull("lastUpdated has been populated", componentTypeCombination.getLastUpdated());
    Assert.assertNotNull("lastUpdatedBy has been populated", componentTypeCombination.getLastUpdatedBy());
  }

  @Test
  public void testIsUniqueCombinationNameForExistingCombination_shouldReturnFalseIfCombinationNameExistsForAnotherCombination() {
    // Test data
    aComponentTypeCombination().withCombinationName("combination1").buildAndPersist(entityManager);
    ComponentTypeCombination combination2 = aComponentTypeCombination()
        .withCombinationName("combination2").buildAndPersist(entityManager);

    // Run test
    boolean unique = componentTypeCombinationRepository.isUniqueCombinationName(combination2.getId(), "combination1");

    // Verify result
    assertThat(unique, is(false));
  }

  @Test
  public void testIsUniqueCombinationNameForExistingCombination_shouldReturnTrueIfCombinationNameExistsForSameCombination() {
    // Test data
    ComponentTypeCombination combination = aComponentTypeCombination()
        .withCombinationName("combination").buildAndPersist(entityManager);

    // Run test
    boolean unique = componentTypeCombinationRepository.isUniqueCombinationName(combination.getId(), "combination");

    // Verify result
    assertThat(unique, is(true));
  }

  @Test
  public void testIsUniqueCombinationNameForNewCombination_shouldReturnFalseIfCombinationNameExists() {
    // Test data
    aComponentTypeCombination()
        .withCombinationName("combination").buildAndPersist(entityManager);
    
    // Run test
    boolean unique = componentTypeCombinationRepository.isUniqueCombinationName(null, "combination");

    // Verify result
    assertThat(unique, is(false));
  }

  @Test
  public void testIsUniqueCombinationNameForNewCombination_shouldReturnTrueIfCombinationNameDoesntExist() {
    // Run test
    boolean unique = componentTypeCombinationRepository.isUniqueCombinationName(null, "combination");

    // Verify result
    assertThat(unique, is(true));
  }

  @Test
  public void testVerifyComponentTypeCombinationExistsWithExistent_shouldReturnTrue() {
    ComponentTypeCombination combination = aComponentTypeCombination().buildAndPersist(entityManager);
    boolean exists = componentTypeCombinationRepository.verifyComponentTypeCombinationExists(combination.getId());
    assertThat(exists, is(true));
  }

  @Test
  public void testVerifyComponentTypeCombinationExistsWithNonExistent_shouldReturnFalse() {
    UUID componentTypeCombinationId = UUID.randomUUID();
    boolean exists = componentTypeCombinationRepository.verifyComponentTypeCombinationExists(componentTypeCombinationId);
    assertThat(exists, is(false));
  }

  @Test
  public void testVerifyComponentTypeCombinationExistsWithDeleted_shouldReturnFalse() {
    ComponentTypeCombination combination = aComponentTypeCombination().thatIsDeleted().buildAndPersist(entityManager);
    boolean exists = componentTypeCombinationRepository.verifyComponentTypeCombinationExists(combination.getId());
    assertThat(exists, is(false));
  }

}
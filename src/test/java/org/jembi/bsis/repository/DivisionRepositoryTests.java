package org.jembi.bsis.repository;

import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;

import java.util.List;

import org.jembi.bsis.model.location.Division;
import org.jembi.bsis.suites.ContextDependentTestSuite;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DivisionRepositoryTests extends ContextDependentTestSuite {

  @Autowired
  private DivisionRepository divisionRepository;
  
  @Test
  public void testFindDivisionByExactName_verifyCorrectDivisionReturned() {
    Division division1 = aDivision().withName("division1").buildAndPersist(entityManager); // match
    Division division2 = aDivision().withName("division2").buildAndPersist(entityManager);
    Division division3 = aDivision().withName("division3").buildAndPersist(entityManager); // match
    Division division4 = aDivision().withName("division4").buildAndPersist(entityManager);
    Division division5 = aDivision().withName("division5").buildAndPersist(entityManager); // match
   
    List<Division> divisions = divisionRepository.findDivisions("division1", false, null);
    
    // Verify only 1 division returned
    Assert.assertEquals("Verify divisions returned", 1, divisions.size());

    // Verify right divions were returned
    Assert.assertTrue("Verify locations", divisions.contains(division1));
    Assert.assertFalse("Verify locations", divisions.contains(division2));
    Assert.assertFalse("Verify locations", divisions.contains(division3));
    Assert.assertFalse("Verify locations", divisions.contains(division4));
    Assert.assertFalse("Verify locations", divisions.contains(division5));
  }

  @Test
  public void testFindDivisionBySimilarName_verifyCorrectDivisionsReturned() {
    Division division1 = aDivision().withName("division1").buildAndPersist(entityManager); // match
    Division division2 = aDivision().withName("division2").buildAndPersist(entityManager);
    Division division3 = aDivision().withName("division3").buildAndPersist(entityManager); // match
    Division division4 = aDivision().withName("division4").buildAndPersist(entityManager);
    Division division5 = aDivision().withName("test").buildAndPersist(entityManager); // match
   
    List<Division> divisions = divisionRepository.findDivisions("division", true, null);
    
    // Verify only 4 divisions are returned
    Assert.assertEquals("Verify divisions returned", 4, divisions.size());

    // Verify right divions were returned
    Assert.assertTrue("Verify locations", divisions.contains(division1));
    Assert.assertTrue("Verify locations", divisions.contains(division2));
    Assert.assertTrue("Verify locations", divisions.contains(division3));
    Assert.assertTrue("Verify locations", divisions.contains(division4));
    Assert.assertFalse("Verify locations", divisions.contains(division5));
  }
  
  @Test
  public void testFindDivisionBySimilarNameAndLevelType_verifyCorrectDivisionsReturned() {
    Division division1 = aDivision().withName("division1").withLevel(1).buildAndPersist(entityManager); // match
    Division division2 = aDivision().withName("division2").withLevel(1).buildAndPersist(entityManager);
    Division division3 = aDivision().withName("division3").withLevel(2).buildAndPersist(entityManager); // match
    Division division4 = aDivision().withName("division4").withLevel(2).buildAndPersist(entityManager);
    Division division5 = aDivision().withName("test").withLevel(1).buildAndPersist(entityManager); // match
   
    List<Division> divisions = divisionRepository.findDivisions("division", true, 2);
    
    // Verify only 2 divisions are returned
    Assert.assertEquals("Verify divisions returned", 2, divisions.size());

    // Verify right divions were returned
    Assert.assertFalse("Verify locations", divisions.contains(division1));
    Assert.assertFalse("Verify locations", divisions.contains(division2));
    Assert.assertTrue("Verify locations", divisions.contains(division3));
    Assert.assertTrue("Verify locations", divisions.contains(division4));
    Assert.assertFalse("Verify locations", divisions.contains(division5));
  }
  
  @Test
  public void testFindDivisionWithParentBySimilarNameAndLevel_2_verifyCorrectDivisionsReturned() {
    Division division1 = aDivision().withName("division1").withLevel(1).buildAndPersist(entityManager); // match
    Division division2 = aDivision().withName("division2").withLevel(1).buildAndPersist(entityManager);
    Division division3 = aDivision().withName("division3").withParent(division1).withLevel(2).buildAndPersist(entityManager); // match
    Division division4 = aDivision().withName("division4").withParent(division1).withLevel(2).buildAndPersist(entityManager);
    Division division5 = aDivision().withName("test").withLevel(1).buildAndPersist(entityManager); // match
   
    List<Division> divisions = divisionRepository.findDivisions("division", true, 2);
    
    // Verify only 2 divisions are returned
    Assert.assertEquals("Verify divisions returned", 2, divisions.size());

    // Verify right divions were returned
    Assert.assertFalse("Verify locations", divisions.contains(division1));
    Assert.assertFalse("Verify locations", divisions.contains(division2));
    Assert.assertTrue("Verify locations", divisions.contains(division3));
    Assert.assertTrue("Verify locations", divisions.contains(division4));
    Assert.assertFalse("Verify locations", divisions.contains(division5));
  }
  
  @Test
  public void testFindDivisionBySimilarNameAndIncorrectLevelType_verifyNoDivisionsReturned() {
    Division division1 = aDivision().withName("division1").withLevel(1).buildAndPersist(entityManager); // match
    Division division2 = aDivision().withName("division2").withLevel(1).buildAndPersist(entityManager);
    Division division3 = aDivision().withName("division3").withLevel(2).buildAndPersist(entityManager); // match
    Division division4 = aDivision().withName("division4").withLevel(2).buildAndPersist(entityManager);
    Division division5 = aDivision().withName("test").withLevel(1).buildAndPersist(entityManager); // match
   
    List<Division> divisions = divisionRepository.findDivisions("division", true, 3);
    
    // Verify no divisions are returned
    Assert.assertEquals("Verify divisions returned", 0, divisions.size());

    // Verify right divions were returned
    Assert.assertFalse("Verify locations", divisions.contains(division1));
    Assert.assertFalse("Verify locations", divisions.contains(division2));
    Assert.assertFalse("Verify locations", divisions.contains(division3));
    Assert.assertFalse("Verify locations", divisions.contains(division4));
    Assert.assertFalse("Verify locations", divisions.contains(division5));
  }
  
  @Test
  public void testFindDivisionBySimilarName_verifyNoDivisionsReturned() {
    Division division1 = aDivision().withName("division1").buildAndPersist(entityManager); // match
    Division division2 = aDivision().withName("division2").buildAndPersist(entityManager);
    Division division3 = aDivision().withName("division3").buildAndPersist(entityManager); // match
    Division division4 = aDivision().withName("division4").buildAndPersist(entityManager);
    Division division5 = aDivision().withName("test").buildAndPersist(entityManager); // match
   
    List<Division> divisions = divisionRepository.findDivisions("tester", true, null);
    
    // Verify no divisions are returned
    Assert.assertEquals("Verify locations returned", 0, divisions.size());

    // Verify right divions were returned
    Assert.assertFalse("Verify locations", divisions.contains(division1));
    Assert.assertFalse("Verify locations", divisions.contains(division2));
    Assert.assertFalse("Verify locations", divisions.contains(division3));
    Assert.assertFalse("Verify locations", divisions.contains(division4));
    Assert.assertFalse("Verify locations", divisions.contains(division5));
  }

}


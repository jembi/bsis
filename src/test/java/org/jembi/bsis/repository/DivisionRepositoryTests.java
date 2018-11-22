package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.DivisionBuilder.aDivision;
import static org.jembi.bsis.helpers.matchers.DivisionMatcher.hasSameStateAsDivision;

import java.util.List;
import java.util.UUID;

import javax.persistence.NoResultException;

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
    Division division3 = aDivision().withName("division3").buildAndPersist(entityManager); 
    Division division4 = aDivision().withName("division4").buildAndPersist(entityManager);
    Division division5 = aDivision().withName("division5").buildAndPersist(entityManager); 

    List<Division> divisions = divisionRepository.findDivisions("division1", false, null, null);

    // Verify only 1 division returned
    Assert.assertEquals("Verify divisions returned", 1, divisions.size());

    // Verify right divisions were returned
    Assert.assertTrue("Verify division 1 present", divisions.contains(division1));
    Assert.assertFalse("Verify division 2 absent", divisions.contains(division2));
    Assert.assertFalse("Verify division 3 absent", divisions.contains(division3));
    Assert.assertFalse("Verify division 4 absent", divisions.contains(division4));
    Assert.assertFalse("Verify division 5 absent", divisions.contains(division5));
  }

  @Test
  public void testFindDivisionBySimilarName_verifyCorrectDivisionsReturned() {
    Division division1 = aDivision().withName("division1").buildAndPersist(entityManager); // match
    Division division2 = aDivision().withName("division2").buildAndPersist(entityManager); // match
    Division division3 = aDivision().withName("division3").buildAndPersist(entityManager); // match
    Division division4 = aDivision().withName("division4").buildAndPersist(entityManager); // match
    Division division5 = aDivision().withName("test").buildAndPersist(entityManager); 

    List<Division> divisions = divisionRepository.findDivisions("division", true, null, null);

    // Verify only 4 divisions are returned
    Assert.assertEquals("Verify divisions returned", 4, divisions.size());

    // Verify right divisions were returned
    Assert.assertTrue("Verify division 1 present", divisions.contains(division1));
    Assert.assertTrue("Verify division 2 present", divisions.contains(division2));
    Assert.assertTrue("Verify division 3 present", divisions.contains(division3));
    Assert.assertTrue("Verify division 4 present", divisions.contains(division4));
    Assert.assertFalse("Verify division 5 absent", divisions.contains(division5));
  }

  @Test
  public void testFindDivisionBySimilarNamePostfix_verifyCorrectDivisionsReturned() {
    Division division1 = aDivision().withName("division1").buildAndPersist(entityManager);
    Division division2 = aDivision().withName("division2").buildAndPersist(entityManager);
    Division division3 = aDivision().withName("division3").buildAndPersist(entityManager);
    Division division4 = aDivision().withName("division4").buildAndPersist(entityManager);
    Division division5 = aDivision().withName("testPostfix").buildAndPersist(entityManager); // match

    List<Division> divisions = divisionRepository.findDivisions("Postfix", true, null, null);

    // Verify only 1 division returned
    Assert.assertEquals("Verify divisions returned", 1, divisions.size());

    // Verify right division was returned
    Assert.assertFalse("Verify division 1 absent", divisions.contains(division1));
    Assert.assertFalse("Verify division 2 absent", divisions.contains(division2));
    Assert.assertFalse("Verify division 3 absent", divisions.contains(division3));
    Assert.assertFalse("Verify division 4 absent", divisions.contains(division4));
    Assert.assertTrue("Verify division 5 present", divisions.contains(division5));
  }

  @Test
  public void testFindDivisionBySimilarNameAndLevelType_verifyCorrectDivisionsReturned() {
    Division division1 = aDivision().withName("division1").withLevel(1).buildAndPersist(entityManager);
    Division division2 = aDivision().withName("division2").withLevel(1).buildAndPersist(entityManager);
    Division division3 = aDivision().withName("division3").withParent(division1).withLevel(2).buildAndPersist(entityManager); // match
    Division division4 = aDivision().withName("division4").withParent(division1).withLevel(2).buildAndPersist(entityManager); // match
    Division division5 = aDivision().withName("test").withLevel(1).buildAndPersist(entityManager); 

    List<Division> divisions = divisionRepository.findDivisions("division", true, 2, null);

    // Verify only 2 divisions are returned
    Assert.assertEquals("Verify divisions returned", 2, divisions.size());

    // Verify right divisions were returned
    Assert.assertFalse("Verify division 1 absent", divisions.contains(division1));
    Assert.assertFalse("Verify division 2 absent", divisions.contains(division2));
    Assert.assertTrue("Verify division 3 present", divisions.contains(division3));
    Assert.assertTrue("Verify division 4 present", divisions.contains(division4));
    Assert.assertFalse("Verify division 5 absent", divisions.contains(division5));
  }

  @Test
  public void testFindDivisionBySimilarNameAndIncorrectLevelType_verifyNoDivisionsReturned() {
    Division division1 = aDivision().withName("division1").withLevel(1).buildAndPersist(entityManager); 
    Division division2 = aDivision().withName("division2").withLevel(1).buildAndPersist(entityManager);
    Division division3 = aDivision().withName("division3").withParent(division1).withLevel(2).buildAndPersist(entityManager); 
    Division division4 = aDivision().withName("division4").withParent(division1).withLevel(2).buildAndPersist(entityManager);
    Division division5 = aDivision().withName("test").withLevel(1).buildAndPersist(entityManager);

    List<Division> divisions = divisionRepository.findDivisions("division", true, 3, null);

    // Verify no divisions are returned
    Assert.assertEquals("Verify divisions returned", 0, divisions.size());

    // Verify right divisions were returned
    Assert.assertFalse("Verify division 1 absent", divisions.contains(division1));
    Assert.assertFalse("Verify division 2 absent", divisions.contains(division2));
    Assert.assertFalse("Verify division 3 absent", divisions.contains(division3));
    Assert.assertFalse("Verify division 4 absent", divisions.contains(division4));
    Assert.assertFalse("Verify division 5 absent", divisions.contains(division5));
  }
  
  @Test
  public void testFindDivisionBySimilarName_verifyNoDivisionsReturned() {
    Division division1 = aDivision().withName("division1").buildAndPersist(entityManager); 
    Division division2 = aDivision().withName("division2").buildAndPersist(entityManager);
    Division division3 = aDivision().withName("division3").buildAndPersist(entityManager); 
    Division division4 = aDivision().withName("division4").buildAndPersist(entityManager);
    Division division5 = aDivision().withName("test").buildAndPersist(entityManager);

    List<Division> divisions = divisionRepository.findDivisions("tester", true, null, null);

    // Verify no divisions are returned
    Assert.assertEquals("Verify divisions returned", 0, divisions.size());

    // Verify right divisions were returned
    Assert.assertFalse("Verify division 1 absent", divisions.contains(division1));
    Assert.assertFalse("Verify division 2 absent", divisions.contains(division2));
    Assert.assertFalse("Verify division 3 absent", divisions.contains(division3));
    Assert.assertFalse("Verify division 4 absent", divisions.contains(division4));
    Assert.assertFalse("Verify division 5 absent", divisions.contains(division5));
  }
  
  @Test
  public void testFindDivisionWithBlankName_verifyAllDivisionsReturned() {
    Division division1 = aDivision().withName("division1").buildAndPersist(entityManager);  // match 
    Division division2 = aDivision().withName("division2").buildAndPersist(entityManager);  // match
    Division division3 = aDivision().withName("division3").buildAndPersist(entityManager);  // match
    Division division4 = aDivision().withName("division4").buildAndPersist(entityManager);  // match
    Division division5 = aDivision().withName("test").buildAndPersist(entityManager);       // match

    List<Division> divisions = divisionRepository.findDivisions("", false, null, null);

    // Verify all divisions are returned
    Assert.assertEquals("Verify all divisions returned", 5, divisions.size());

    // Verify right divisions were returned
    Assert.assertTrue("Verify division 1 present", divisions.contains(division1));
    Assert.assertTrue("Verify division 2 present", divisions.contains(division2));
    Assert.assertTrue("Verify division 3 present", divisions.contains(division3));
    Assert.assertTrue("Verify division 4 present", divisions.contains(division4));
    Assert.assertTrue("Verify division 5 present", divisions.contains(division5));
  }

  @Test
  public void testFindDivisionBySimilarNameAndLevelTypeAndParent_verifyCorrectDivisionsReturned() {
    Division division1 = aDivision().withName("division1").withLevel(1).buildAndPersist(entityManager);
    Division division2 = aDivision().withName("division2").withLevel(1).buildAndPersist(entityManager);
    Division division3 = aDivision().withName("division3").withParent(division1).withLevel(2).buildAndPersist(entityManager); // match
    Division division4 = aDivision().withName("division4").withParent(division2).withLevel(2).buildAndPersist(entityManager);
    Division division5 = aDivision().withName("test").withLevel(1).buildAndPersist(entityManager); 

    List<Division> divisions = divisionRepository.findDivisions("division", true, 2, division1.getId());

    // Verify only 1 divisions are returned
    Assert.assertEquals("Verify divisions returned", 1, divisions.size());

    // Verify right divisions were returned
    Assert.assertFalse("Verify division 1 absent", divisions.contains(division1));
    Assert.assertFalse("Verify division 2 absent", divisions.contains(division2));
    Assert.assertTrue("Verify division 3 present", divisions.contains(division3));
    Assert.assertFalse("Verify division 4 absent", divisions.contains(division4));
    Assert.assertFalse("Verify division 5 absent", divisions.contains(division5));
  }
  
  @Test
  public void testFindDivisionByLevelTypeAndParent_verifyCorrectDivisionsReturned() {
    Division division1 = aDivision().withName("division1").withLevel(1).buildAndPersist(entityManager);
    Division division2 = aDivision().withName("division2").withLevel(1).buildAndPersist(entityManager);
    Division division3 = aDivision().withName("division3").withParent(division1).withLevel(2).buildAndPersist(entityManager); // match
    Division division4 = aDivision().withName("division4").withParent(division2).withLevel(2).buildAndPersist(entityManager);
    Division division5 = aDivision().withName("test").withParent(division1).withLevel(2).buildAndPersist(entityManager); // match

    List<Division> divisions = divisionRepository.findDivisions("", true, 2, division1.getId());

    // Verify only 2 divisions are returned
    Assert.assertEquals("Verify divisions returned", 2, divisions.size());

    // Verify right divisions were returned
    Assert.assertFalse("Verify division 1 absent", divisions.contains(division1));
    Assert.assertFalse("Verify division 2 absent", divisions.contains(division2));
    Assert.assertTrue("Verify division 3 present", divisions.contains(division3));
    Assert.assertFalse("Verify division 4 absent", divisions.contains(division4));
    Assert.assertTrue("Verify division 5 present", divisions.contains(division5));
  }
  
  @Test
  public void testFindDivisionByParent_verifyCorrectDivisionsReturned() {
    Division division1 = aDivision().withName("division1").withLevel(1).buildAndPersist(entityManager);
    Division division2 = aDivision().withName("division2").withLevel(1).buildAndPersist(entityManager);
    Division division3 = aDivision().withName("division3").withParent(division1).withLevel(2).buildAndPersist(entityManager); // match
    Division division4 = aDivision().withName("division4").withParent(division2).withLevel(2).buildAndPersist(entityManager);
    Division division5 = aDivision().withName("test").withParent(division1).withLevel(2).buildAndPersist(entityManager); // match

    List<Division> divisions = divisionRepository.findDivisions("", true, 2, division1.getId());

    // Verify only 2 divisions are returned
    Assert.assertEquals("Verify divisions returned", 2, divisions.size());

    // Verify right divisions were returned
    Assert.assertFalse("Verify division 1 absent", divisions.contains(division1));
    Assert.assertFalse("Verify division 2 absent", divisions.contains(division2));
    Assert.assertTrue("Verify division 3 present", divisions.contains(division3));
    Assert.assertFalse("Verify division 4 absent", divisions.contains(division4));
    Assert.assertTrue("Verify division 5 present", divisions.contains(division5));
  }

  @Test
  public void testFindDivisionWithNullValues_verifyAllDivisionsReturned() {
    Division division1 = aDivision().withName("division1").buildAndPersist(entityManager); // match  
    Division division2 = aDivision().withName("division2").buildAndPersist(entityManager); // match
    Division division3 = aDivision().withName("division3").buildAndPersist(entityManager); // match
    Division division4 = aDivision().withName("division4").buildAndPersist(entityManager); // match
    Division division5 = aDivision().withName("test").buildAndPersist(entityManager);      // match

    List<Division> divisions = divisionRepository.findDivisions(null, false, null, null);

    // Verify all divisions are returned
    Assert.assertEquals("Verify all divisions returned", 5, divisions.size());

    // Verify right divisions were returned
    Assert.assertTrue("Verify division 1 present", divisions.contains(division1));
    Assert.assertTrue("Verify division 2 present", divisions.contains(division2));
    Assert.assertTrue("Verify division 3 present", divisions.contains(division3));
    Assert.assertTrue("Verify division 4 present", divisions.contains(division4));
    Assert.assertTrue("Verify division 5 present", divisions.contains(division5));
  }
  
  @Test
  public void testFindDivisionWithBlankNameAndLevel2_verifyCorrectDivisionsReturned() {
    Division division1 = aDivision().withName("division1").buildAndPersist(entityManager);  
    Division division2 = aDivision().withName("division2").buildAndPersist(entityManager);
    Division division3 = aDivision().withName("division3").withLevel(2).buildAndPersist(entityManager); // match  
    Division division4 = aDivision().withName("division4").withLevel(2).buildAndPersist(entityManager); // match  
    Division division5 = aDivision().withName("test").withLevel(2).buildAndPersist(entityManager);      // match  

    List<Division> divisions = divisionRepository.findDivisions("", false, 2, null);

    // Verify correct amount of divisions are returned
    Assert.assertEquals("Verify all divisions returned", 3, divisions.size());

    // Verify right divisions were returned
    Assert.assertFalse("Verify division 1 absent", divisions.contains(division1));
    Assert.assertFalse("Verify division 2 absent", divisions.contains(division2));
    Assert.assertTrue("Verify division 3 present", divisions.contains(division3));
    Assert.assertTrue("Verify division 4 present", divisions.contains(division4));
    Assert.assertTrue("Verify division 5 present", divisions.contains(division5));
  }
  
  @Test
  public void testFindDivisionById_shouldReturnCorrectDivision() {
    // Set up fixture
    aDivision().withName("name1").buildAndPersist(entityManager);
    Division expectedDivision = aDivision().withName("name2").buildAndPersist(entityManager);
    aDivision().withName("name3").buildAndPersist(entityManager);
    
    // Exercise SUT
    Division returnedDivision = divisionRepository.findDivisionById(expectedDivision.getId());
    
    // Verify
    assertThat(returnedDivision, is(expectedDivision));
  }
  
  @Test(expected = NoResultException.class)
  public void testFindDivisionByIdWithMissingDivision_shouldThrow() {
    // Exercise SUT
    divisionRepository.findDivisionById(UUID.randomUUID());
  }
  
  @Test
  public void testCountDivisionsWithParent_shouldReturnCorrectCount() {
    Division parent = aDivision().withName("parentName1").buildAndPersist(entityManager);
    
    // Expected
    aDivision().withName("name2").withParent(parent).buildAndPersist(entityManager);
    // Excluded by parent
    aDivision().withName("name3").withParent(aDivision().withName("parentName2").build()).buildAndPersist(entityManager);
    // Expected
    aDivision().withName("name4").withParent(parent).buildAndPersist(entityManager);
    // Excluded by no parent
    aDivision().withName("name5").withParent(null).buildAndPersist(entityManager);
    
    long returnedCount = divisionRepository.countDivisionsWithParent(parent);
    
    assertThat(returnedCount, is(2L));
  }
  
  @Test
  public void testGetAllDivisions_verifyAllDivisionsReturned() {
    Division division1 = aDivision().withName("division1").buildAndPersist(entityManager); // match
    Division division2 = aDivision().withName("division2").buildAndPersist(entityManager); // match
    Division division3 = aDivision().withName("division3").withLevel(2).buildAndPersist(entityManager); // match  
    Division division4 = aDivision().withName("division4").withLevel(2).buildAndPersist(entityManager); // match  
    Division division5 = aDivision().withName("test").withLevel(2).buildAndPersist(entityManager); // match

    List<Division> divisions = divisionRepository.getAllDivisions();

    // Verify correct amount of divisions are returned
    Assert.assertEquals("Verify all divisions returned", 5, divisions.size());

    // Verify all divisions were returned
    Assert.assertTrue("Verify division 1 present", divisions.contains(division1));
    Assert.assertTrue("Verify division 2 present", divisions.contains(division2));
    Assert.assertTrue("Verify division 3 present", divisions.contains(division3));
    Assert.assertTrue("Verify division 4 present", divisions.contains(division4));
    Assert.assertTrue("Verify division 5 present", divisions.contains(division5));
  }

  @Test
  public void testFindDivisionByName_verifyCorrectDivisionReturned() {
    Division division1 = aDivision().withName("division1").buildAndPersist(entityManager); // match
    aDivision().withName("division2").buildAndPersist(entityManager);

    Division divisionFound = divisionRepository.findDivisionByName("division1");

    assertThat(divisionFound, hasSameStateAsDivision(division1));
  }

  @Test
  public void testFindDivisionByName_verifyNullReturned() {
    aDivision().withName("division1").buildAndPersist(entityManager);
    aDivision().withName("division2").buildAndPersist(entityManager);

    Division divisionFound = divisionRepository.findDivisionByName("division3");

    Assert.assertNull(divisionFound);
  }

}

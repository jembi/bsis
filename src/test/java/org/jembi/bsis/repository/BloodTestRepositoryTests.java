package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBasicBloodTypingBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBasicTTIBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aConfirmatoryTTIBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aRepeatBloodTypingBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aRepeatTTIBloodTest;
import static org.jembi.bsis.helpers.matchers.BloodTestMatcher.hasSameStateAsBloodTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class BloodTestRepositoryTests extends SecurityContextDependentTestSuite {
  
  @Autowired
  private BloodTestRepository bloodTestRepository;

  @Test
  public void testGetBloodTypingTests_shouldReturnCorrectBloodTests() {
    // Set up data
    BloodTest aboTest = aBasicBloodTypingBloodTest().buildAndPersist(entityManager);
    BloodTest repeatAboTest = aRepeatBloodTypingBloodTest().buildAndPersist(entityManager);
    aBasicTTIBloodTest().buildAndPersist(entityManager); // excluded by type
    aBasicBloodTypingBloodTest().thatIsInActive().buildAndPersist(entityManager); // excluded by inactive
    aBasicBloodTypingBloodTest().thatIsDeleted().buildAndPersist(entityManager); // excluded by deleted

    // Test
    List<BloodTest> bloodTypingTests = bloodTestRepository.getBloodTypingTests();

    // Verify
    assertThat("2 tests returned", bloodTypingTests.size(), is(2));
    assertThat("aboTest is returned", bloodTypingTests.contains(aboTest));
    assertThat("repeatAboTest is returned", bloodTypingTests.contains(repeatAboTest));
  }

  @Test
  public void testGetBloodTypingTests_shouldReturnBloodTestsInCorrectOrder() {
    // Set up data
    BloodTest aboTest1 = aBasicBloodTypingBloodTest().withRankInCategory(null).buildAndPersist(entityManager);
    BloodTest aboTest2 = aBasicBloodTypingBloodTest().withRankInCategory(2).buildAndPersist(entityManager);
    BloodTest aboTest3 = aBasicBloodTypingBloodTest().withRankInCategory(1).buildAndPersist(entityManager);

    // Test
    List<BloodTest> bloodTypingTests = bloodTestRepository.getBloodTypingTests();

    // Verify
    assertThat(bloodTypingTests.size(), is(3));
    assertThat(bloodTypingTests.get(0), is(aboTest3));
    assertThat(bloodTypingTests.get(1), is(aboTest2));
    assertThat(bloodTypingTests.get(2), is(aboTest1));
  }

  @Test
  public void testGetBloodTestsOfType_shouldReturnCorrectBloodTests() {
    // Set up data
    BloodTest aboTest = aBasicBloodTypingBloodTest().buildAndPersist(entityManager);
    BloodTest rhTest = aBasicBloodTypingBloodTest().buildAndPersist(entityManager);
    aRepeatBloodTypingBloodTest().buildAndPersist(entityManager); // excluded by type
    aBasicTTIBloodTest().buildAndPersist(entityManager); // excluded by type
    aBasicBloodTypingBloodTest().thatIsInActive().buildAndPersist(entityManager); // excluded by inactive
    aBasicBloodTypingBloodTest().thatIsDeleted().buildAndPersist(entityManager); // excluded by deleted

    // Test
    List<BloodTest> basicBloodTypingTests = bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING);

    // Verify
    assertThat("2 tests returned", basicBloodTypingTests.size(), is(2));
    assertThat("aboTest is returned", basicBloodTypingTests.contains(aboTest));
    assertThat("rhTest is returned", basicBloodTypingTests.contains(rhTest));
  }

  @Test
  public void testGetBloodTestsOfType_shouldReturnBloodTestsInCorrectOrder() {
    // Set up data
    BloodTest aboTest1 = aBasicBloodTypingBloodTest().withRankInCategory(null).buildAndPersist(entityManager);
    BloodTest aboTest2 = aBasicBloodTypingBloodTest().withRankInCategory(1).buildAndPersist(entityManager);
    BloodTest aboTest3 = aBasicBloodTypingBloodTest().withRankInCategory(2).buildAndPersist(entityManager);

    // Test
    List<BloodTest> basicBloodTypingTests = bloodTestRepository.getBloodTestsOfType(BloodTestType.BASIC_BLOODTYPING);

    // Verify
    assertThat(basicBloodTypingTests.size(), is(3));
    assertThat(basicBloodTypingTests.get(0), is(aboTest2));
    assertThat(basicBloodTypingTests.get(1), is(aboTest3));
    assertThat(basicBloodTypingTests.get(2), is(aboTest1));
  }

  @Test
  public void testGetEnabledBloodTestsOfType_shouldReturnCorrectBloodTests() {
    // Set up data
    BloodTest enabledActiveTest = aBasicTTIBloodTest().buildAndPersist(entityManager);
    BloodTest enabledInactiveTest = aBasicTTIBloodTest().thatIsInActive().buildAndPersist(entityManager);
    aBasicTTIBloodTest().thatIsDeleted().thatIsInActive().buildAndPersist(entityManager); // excluded by deleted
    aBasicTTIBloodTest().thatIsDeleted().buildAndPersist(entityManager); // excluded by deleted

    // Test
    List<BloodTest> basicTTITests = bloodTestRepository.getEnabledBloodTestsOfType(BloodTestType.BASIC_TTI);

    // Verify
    assertThat(basicTTITests.size(), is(2));
    assertThat(basicTTITests, hasItem(enabledActiveTest));
    assertThat(basicTTITests, hasItem(enabledInactiveTest));
  }

  @Test
  public void testGetBloodTestsThatAreActiveAndNotDeleted_shouldReturnCorrectBloodTests() {
    // Set up data
    BloodTest ttiTest = aBasicTTIBloodTest().buildAndPersist(entityManager);
    BloodTest aboTest = aBasicBloodTypingBloodTest().buildAndPersist(entityManager);
    aBloodTest().thatIsInActive().buildAndPersist(entityManager);
    aBloodTest().thatIsDeleted().buildAndPersist(entityManager);
    
    // Test
    List<BloodTest> activeBloodTests = bloodTestRepository.getBloodTests(false, false);

    // Verify
    assertThat("2 tests returned", activeBloodTests.size(), is(2));
    assertThat("ttiTest is returned", activeBloodTests.contains(ttiTest));
    assertThat("aboTest is returned", activeBloodTests.contains(aboTest));
  }

  @Test
  public void testGetBloodTests_shouldReturnBloodTestsInCorrectOrder() {
    // Set up data
    BloodTest ttiTest1 = aBasicTTIBloodTest().withRankInCategory(1).buildAndPersist(entityManager);
    BloodTest ttiTest2 = aBasicTTIBloodTest().withRankInCategory(2).buildAndPersist(entityManager);
    BloodTest aboTest1 = aRepeatBloodTypingBloodTest().withRankInCategory(3).buildAndPersist(entityManager);
    BloodTest aboTest2 = aBasicBloodTypingBloodTest().withRankInCategory(2).buildAndPersist(entityManager);
    BloodTest aboTest3 = aBasicBloodTypingBloodTest().withRankInCategory(1).buildAndPersist(entityManager);
    
    // Test
    List<BloodTest> activeBloodTests = bloodTestRepository.getBloodTests(false, false);

    // Verify
    assertThat(activeBloodTests.size(), is(5));
    assertThat(activeBloodTests.get(0), is(aboTest3));
    assertThat(activeBloodTests.get(1), is(aboTest2));
    assertThat(activeBloodTests.get(2), is(aboTest1));
    assertThat(activeBloodTests.get(3), is(ttiTest1));
    assertThat(activeBloodTests.get(4), is(ttiTest2));
  }

  @Test
  public void testGetBloodTestsIncludeInactiveAndDeleted_shouldReturnAllBloodTests() {
    // Set up data
    BloodTest ttiTest = aBasicTTIBloodTest().buildAndPersist(entityManager);
    BloodTest aboTest = aBasicBloodTypingBloodTest().buildAndPersist(entityManager);
    BloodTest inactiveTest = aBloodTest().thatIsInActive().buildAndPersist(entityManager);
    BloodTest deletedTest = aBloodTest().thatIsDeleted().buildAndPersist(entityManager);
    
    // Test
    List<BloodTest> activeBloodTests = bloodTestRepository.getBloodTests(true, true);

    // Verify
    assertThat("4 tests returned", activeBloodTests.size(), is(4));
    assertThat("ttiTest is returned", activeBloodTests.contains(ttiTest));
    assertThat("aboTest is returned", activeBloodTests.contains(aboTest));
    assertThat("inactiveTest is returned", activeBloodTests.contains(inactiveTest));
    assertThat("deletedTest is returned", activeBloodTests.contains(deletedTest));
  }

  @Test
  public void testGetBloodTestsIncludeInactiveButNotDeleted_shouldAllNotDeletedBloodTests() {
    // Set up data
    BloodTest ttiTest = aBasicTTIBloodTest().buildAndPersist(entityManager);
    BloodTest aboTest = aBasicBloodTypingBloodTest().buildAndPersist(entityManager);
    BloodTest inactiveTest = aBloodTest().thatIsInActive().buildAndPersist(entityManager);
    aBloodTest().thatIsDeleted().buildAndPersist(entityManager);
    
    // Test
    List<BloodTest> activeBloodTests = bloodTestRepository.getBloodTests(true, false);

    // Verify
    assertThat("3 tests returned", activeBloodTests.size(), is(3));
    assertThat("ttiTest is returned", activeBloodTests.contains(ttiTest));
    assertThat("aboTest is returned", activeBloodTests.contains(aboTest));
    assertThat("inactiveTest is returned", activeBloodTests.contains(inactiveTest));
  }

  @Test
  public void testGetBloodTestsIncludeDeletedButNotInactive_shouldAllNotDeletedBloodTests() {
    // Set up data
    BloodTest ttiTest = aBasicTTIBloodTest().buildAndPersist(entityManager);
    BloodTest aboTest = aBasicBloodTypingBloodTest().buildAndPersist(entityManager);
    aBloodTest().thatIsInActive().buildAndPersist(entityManager);
    BloodTest deletedTest = aBloodTest().thatIsDeleted().buildAndPersist(entityManager);
    
    // Test
    List<BloodTest> activeBloodTests = bloodTestRepository.getBloodTests(false, true);

    // Verify
    assertThat("3 tests returned", activeBloodTests.size(), is(3));
    assertThat("ttiTest is returned", activeBloodTests.contains(ttiTest));
    assertThat("aboTest is returned", activeBloodTests.contains(aboTest));
    assertThat("deletedTest is returned", activeBloodTests.contains(deletedTest));
  }
  
  @Test
  public void testIsUniqueTestNameForExistingBloodTest_shouldReturnFalseIfTestnameExistsForAnotherBloodTest() {
    // Test data
    aBloodTest()
    .withTestName("test name 1")
    .withTestNameShort("test short name 1")
    .buildAndPersist(entityManager);
    
    BloodTest bloodTest2 = aBloodTest()
        .withTestName("test name 2")
        .withTestNameShort("test short name 2")
        .buildAndPersist(entityManager);

    // Run test
    boolean unique = bloodTestRepository.isUniqueTestName(bloodTest2.getId(), "test name 1");

    // Verify result
    assertThat(unique, is(false));
  }
  
  @Test
  public void testIsUniqueTestNameForExistingBloodTest_shouldReturnTrueIfTestNameExistsForSameBloodTest() {
    // Test data
    
    BloodTest bloodTest = aBloodTest()
        .withTestName("test name")
        .withTestNameShort("test short name")
        .buildAndPersist(entityManager);

    // Run test
    boolean unique = bloodTestRepository.isUniqueTestName(bloodTest.getId(), "test name");

    // Verify result
    assertThat(unique, is(true));
  }

  @Test
  public void testIsUniqueTestNameForNewBloodTest_shouldReturnFalseIfBloodTestNameExists() {
    // Test data
    aBloodTest()
    .withTestName("test name")
    .withTestNameShort("test short name")
    .buildAndPersist(entityManager);
    
    // Run test
    boolean unique = bloodTestRepository.isUniqueTestName(null, "test name");

    // Verify result
    assertThat(unique, is(false));
  }

  @Test
  public void testIsUniqueTestNameForNewBloodTest_shouldReturnTrueIfBloodTestNameDoesntExist() {
    // Run test
    boolean unique = bloodTestRepository.isUniqueTestName(null, "test name");

    // Verify result
    assertThat(unique, is(true));
  }

  @Test
  public void testVerifyBloodTestExists_shouldReturnTrue() {
    BloodTest bloodTest = aBloodTest().buildAndPersist(entityManager);
    boolean exists = bloodTestRepository.verifyBloodTestExists(bloodTest.getId());
    assertThat(exists, is(true));
  }

  @Test
  public void testVerifyBloodTestExists_shouldReturnFalse() {
    boolean exists = bloodTestRepository.verifyBloodTestExists(UUID.randomUUID());
    assertThat(exists, is(false));
  }
  
  @Test
  public void testGetBloodTestsThatAreActiveAndNotDeleted_shouldReturnBloodTestsInCorrectOrder() {
    // Set up data
    List<BloodTest> expectedBloodTest = Arrays.asList(
        aConfirmatoryTTIBloodTest().withRankInCategory(3).buildAndPersist(entityManager), 
        aRepeatTTIBloodTest().withRankInCategory(2).buildAndPersist(entityManager),
        aBasicTTIBloodTest().withRankInCategory(1).buildAndPersist(entityManager)
        
    );
       
    // Test
    List<BloodTest> activeBloodTests = bloodTestRepository.getBloodTests(false, false);

    // Verify
    assertThat(activeBloodTests.size(), is(3));
    assertThat(activeBloodTests.get(0), hasSameStateAsBloodTest(expectedBloodTest.get(2)));
    assertThat(activeBloodTests.get(1), hasSameStateAsBloodTest(expectedBloodTest.get(1)));
    assertThat(activeBloodTests.get(2), hasSameStateAsBloodTest(expectedBloodTest.get(0)));
  }
}

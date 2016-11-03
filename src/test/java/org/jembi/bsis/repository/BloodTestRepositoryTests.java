package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBasicBloodTypingBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aRepeatBloodTypingBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBasicTTIBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;

import java.util.List;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.repository.bloodtesting.BloodTestRepository;
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
}

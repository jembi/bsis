package org.jembi.bsis.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBasicBloodTypingBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBasicTTIBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;

import java.util.List;

import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.repository.bloodtesting.BloodTestRepository;
import org.jembi.bsis.suites.SecurityContextDependentTestSuite;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class BloodTestRepositoryTests extends SecurityContextDependentTestSuite {
  
  @Autowired
  private BloodTestRepository bloodTestRepository;

  @Test
  public void testFindActiveBloodTests_shouldReturnActiveBloodTests() {
    // active tests
    BloodTest ttiTest = aBasicTTIBloodTest().withTestName("TTI").withTestNameShort("tti").buildAndPersist(entityManager);
    BloodTest aboTest = aBasicBloodTypingBloodTest().withTestName("ABO").withTestNameShort("abo").buildAndPersist(entityManager);
    // inactive test
    aBloodTest().thatIsInActive().withTestName("Inactive").withTestNameShort("inact").buildAndPersist(entityManager);
    // deleted test
    aBloodTest().thatIsDeleted().withTestName("Deleted").withTestNameShort("del").buildAndPersist(entityManager);
    
    // Test
    List<BloodTest> activeBloodTests = bloodTestRepository.findActiveBloodTests();

    // Verify
    assertThat("2 tests returned", activeBloodTests.size(), is(2));
    assertThat("ttiTest is returned", activeBloodTests.contains(ttiTest));
    assertThat("aboTest is returned", activeBloodTests.contains(aboTest));
  }

  @Test
  public void testGetAllBloodTestsIncludeInactive_shouldReturnAllBloodTests() {
    // active tests
    BloodTest ttiTest = aBasicTTIBloodTest().withTestName("TTI").withTestNameShort("tti").buildAndPersist(entityManager);
    BloodTest aboTest = aBasicBloodTypingBloodTest().withTestName("ABO").withTestNameShort("abo").buildAndPersist(entityManager);
    // inactive test
    BloodTest inactiveTest = aBloodTest().thatIsInActive().withTestName("Inactive").withTestNameShort("inact").buildAndPersist(entityManager);
    // deleted test
    BloodTest deletedTest = aBloodTest().thatIsDeleted().withTestName("Deleted").withTestNameShort("del").buildAndPersist(entityManager);
    
    // Test
    List<BloodTest> activeBloodTests = bloodTestRepository.getAllBloodTestsIncludeInactive();

    // Verify
    assertThat("4 tests returned", activeBloodTests.size(), is(4));
    assertThat("ttiTest is returned", activeBloodTests.contains(ttiTest));
    assertThat("aboTest is returned", activeBloodTests.contains(aboTest));
    assertThat("inactiveTest is returned", activeBloodTests.contains(inactiveTest));
    assertThat("deletedTest is returned", activeBloodTests.contains(deletedTest));
  }
}

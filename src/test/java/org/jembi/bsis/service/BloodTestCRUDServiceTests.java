package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.matchers.BloodTestMatcher.hasSameStateAsBloodTest;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.jembi.bsis.helpers.builders.BloodTestBuilder;
import org.jembi.bsis.model.bloodtesting.BloodTest;
import org.jembi.bsis.model.bloodtesting.BloodTestCategory;
import org.jembi.bsis.model.bloodtesting.BloodTestType;
import org.jembi.bsis.repository.BloodTestRepository;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class BloodTestCRUDServiceTests extends UnitTestSuite {

  @InjectMocks
  private BloodTestCRUDService bloodTestCRUDService;

  @Mock
  private BloodTestRepository bloodTestRepository;
  
  @Test
  public void testCreateBloodTest_shouldSave() {
    // Set up data
    BloodTest bloodTest = BloodTestBuilder.aBloodTest()
        .withTestName("testName")
        .withTestNameShort("testNameShort")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
        .build();

    // Run test
    bloodTest = bloodTestCRUDService.createBloodTest(bloodTest);
    
    // Verify
    verify(bloodTestRepository).save(argThat(hasSameStateAsBloodTest(bloodTest)));
  }
  
  @Test
  public void testUpdateBloodTest_shouldUpdate() {
    // Set up data
    UUID bloodTestId = UUID.randomUUID();
    BloodTest existingBloodTest = BloodTestBuilder.aBloodTest()
        .withId(bloodTestId)
        .withTestName("testName")
        .withTestNameShort("testNameShort")
        .withCategory(BloodTestCategory.BLOODTYPING)
        .withBloodTestType(BloodTestType.BASIC_BLOODTYPING)
        .build();
    
    BloodTest updatedBloodTest = BloodTestBuilder.aBloodTest()
        .withId(bloodTestId)
        .withTestName("testName1")
        .withTestNameShort("testNameShort1")
        .withCategory(BloodTestCategory.TTI)
        .withBloodTestType(BloodTestType.BASIC_TTI)
        .build();
    
    // Set up mocks
    when(bloodTestRepository.findBloodTestById(bloodTestId)).thenReturn(existingBloodTest);
    when(bloodTestRepository.update(existingBloodTest)).thenReturn(updatedBloodTest);

    // Run test
    BloodTest returnedBloodTest = bloodTestCRUDService.updateBloodTest(updatedBloodTest);
    
    // Verify
    verify(bloodTestRepository).update(argThat(hasSameStateAsBloodTest(updatedBloodTest)));
    assertThat(returnedBloodTest, hasSameStateAsBloodTest(updatedBloodTest));
  }
}

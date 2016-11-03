package org.jembi.bsis.service;

import static org.jembi.bsis.helpers.matchers.BloodTestMatcher.hasSameStateAsBloodTest;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;

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
}

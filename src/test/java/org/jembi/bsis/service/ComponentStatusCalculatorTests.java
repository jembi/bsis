package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.jembi.bsis.helpers.builders.BloodTestBuilder.aBloodTest;
import static org.jembi.bsis.helpers.builders.BloodTestResultBuilder.aBloodTestResult;

import java.util.Arrays;
import java.util.List;

import org.jembi.bsis.model.bloodtesting.BloodTestResult;
import org.jembi.bsis.service.ComponentStatusCalculator;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;
import org.mockito.InjectMocks;

public class ComponentStatusCalculatorTests extends UnitTestSuite {

  @InjectMocks
  private ComponentStatusCalculator componentStatusCalculator;

  @Test
  public void testShouldComponentsBeDiscardedWithBloodTestNotFlaggedForDiscard_shouldReturnFalse() {

    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult()
            .withId(9L)
            .withResult("POS")
            .withBloodTest(aBloodTest()
                .withFlagComponentsForDiscard(false)
                .withPositiveResults("POS,+")
                .build())
            .build()
    );

    boolean result = componentStatusCalculator.shouldComponentsBeDiscarded(bloodTestResults);

    assertThat(result, is(false));
  }

  @Test
  public void testShouldComponentsBeDiscardedWithBloodTestFlaggedForDiscardWithNegativeResult_shouldReturnFalse() {

    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult()
            .withId(9L)
            .withResult("NEG")
            .withBloodTest(aBloodTest()
                .withFlagComponentsForDiscard(true)
                .withPositiveResults("POS,+")
                .build())
            .build()
    );

    boolean result = componentStatusCalculator.shouldComponentsBeDiscarded(bloodTestResults);

    assertThat(result, is(false));
  }

  @Test
  public void testShouldComponentsBeDiscardedWithBloodTestFlaggedForDiscardWithPositiveResult_shouldReturnTrue() {

    List<BloodTestResult> bloodTestResults = Arrays.asList(
        aBloodTestResult()
            .withId(9L)
            .withResult("POS")
            .withBloodTest(aBloodTest()
                .withFlagComponentsForDiscard(true)
                .withPositiveResults("POS,+")
                .build())
            .build()
    );

    boolean result = componentStatusCalculator.shouldComponentsBeDiscarded(bloodTestResults);

    assertThat(result, is(true));
  }

}

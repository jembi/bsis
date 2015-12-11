package service;

import model.bloodtesting.BloodTestResult;
import org.junit.Test;
import org.mockito.InjectMocks;
import suites.UnitTestSuite;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static helpers.builders.BloodTestBuilder.aBloodTest;
import static helpers.builders.BloodTestResultBuilder.aBloodTestResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ComponentStatusCalculatorTests extends UnitTestSuite {

  @InjectMocks
  private ComponentStatusCalculator componentStatusCalculator;

  @Test
  public void testShouldComponentsBeDiscardedWithBloodTestNotFlaggedForDiscard_shouldReturnFalse() {

    List<BloodTestResult> bloodTestResults = Collections.singletonList(
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

    List<BloodTestResult> bloodTestResults = Collections.singletonList(
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

    List<BloodTestResult> bloodTestResults = Collections.singletonList(
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

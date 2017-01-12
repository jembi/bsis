package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.bloodtesting.BloodTest;

public class BloodTestMatcher extends TypeSafeMatcher<BloodTest> {
  
  private BloodTest expected;

  public BloodTestMatcher(BloodTest expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A BloodTest with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nTestName: ").appendValue(expected.getTestName())
        .appendText("\nTestNameShort: ").appendValue(expected.getTestNameShort())
        .appendText("\nCategory: ").appendValue(expected.getCategory())
        .appendText("\nBloodTestType: ").appendValue(expected.getBloodTestType())
        .appendText("\nValidResults: ").appendValue(expected.getValidResults())
        .appendText("\nPositiveResults: ").appendValue(expected.getPositiveResults())
        .appendText("\nNegativeResults: ").appendValue(expected.getNegativeResults())
        .appendText("\nIsActive: ").appendValue(expected.getIsActive())
        .appendText("\nIsDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nFlagComponentsContainingPlasmaForDiscard: ").appendValue(expected.getFlagComponentsContainingPlasmaForDiscard())
        .appendText("\nFlagComponentsForDiscard: ").appendValue(expected.isFlagComponentsForDiscard());
  }

  @Override
  protected boolean matchesSafely(BloodTest actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getTestName(), expected.getTestName())
        && Objects.equals(actual.getTestNameShort(), expected.getTestNameShort())
        && Objects.equals(actual.getCategory(), expected.getCategory())
        && Objects.equals(actual.getBloodTestType(), expected.getBloodTestType())
        && Objects.equals(actual.getValidResults(), expected.getValidResults())
        && Objects.equals(actual.getPositiveResults(), expected.getPositiveResults())
        && Objects.equals(actual.getNegativeResults(), expected.getNegativeResults())
        && Objects.equals(actual.getIsActive(), expected.getIsActive())
        && Objects.equals(actual.getIsDeleted(), expected.getIsDeleted())
        && Objects.equals(actual.getFlagComponentsContainingPlasmaForDiscard(), expected.getFlagComponentsContainingPlasmaForDiscard())
        && Objects.equals(actual.isFlagComponentsForDiscard(), expected.isFlagComponentsForDiscard());
  }
  
  public static BloodTestMatcher hasSameStateAsBloodTest(BloodTest expected) {
    return new BloodTestMatcher(expected);
  }
}

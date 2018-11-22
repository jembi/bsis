package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.model.bloodtesting.BloodTest;

public class BloodTestMatcher extends AbstractTypeSafeMatcher<BloodTest> {

  public BloodTestMatcher(BloodTest expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, BloodTest bloodTest) {
    description.appendText("A BloodTest with the following state:")
        .appendText("\nId: ").appendValue(bloodTest.getId())
        .appendText("\nTestName: ").appendValue(bloodTest.getTestName())
        .appendText("\nTestNameShort: ").appendValue(bloodTest.getTestNameShort())
        .appendText("\nCategory: ").appendValue(bloodTest.getCategory())
        .appendText("\nBloodTestType: ").appendValue(bloodTest.getBloodTestType())
        .appendText("\nValidResults: ").appendValue(bloodTest.getValidResults())
        .appendText("\nPositiveResults: ").appendValue(bloodTest.getPositiveResults())
        .appendText("\nNegativeResults: ").appendValue(bloodTest.getNegativeResults())
        .appendText("\nIsActive: ").appendValue(bloodTest.getIsActive())
        .appendText("\nIsDeleted: ").appendValue(bloodTest.getIsDeleted())
        .appendText("\nFlagComponentsContainingPlasmaForDiscard: ").appendValue(bloodTest.getFlagComponentsContainingPlasmaForDiscard())
        .appendText("\nFlagComponentsForDiscard: ").appendValue(bloodTest.isFlagComponentsForDiscard())
        .appendText("\nRankInCategory: ").appendValue(bloodTest.getRankInCategory());
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
        && Objects.equals(actual.isFlagComponentsForDiscard(), expected.isFlagComponentsForDiscard())
        && Objects.equals(actual.getRankInCategory(), expected.getRankInCategory());
    
  }
  
  public static BloodTestMatcher hasSameStateAsBloodTest(BloodTest expected) {
    return new BloodTestMatcher(expected);
  }
}

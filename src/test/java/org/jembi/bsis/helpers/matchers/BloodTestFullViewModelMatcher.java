package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.BloodTestFullViewModel;

public class BloodTestFullViewModelMatcher extends TypeSafeMatcher<BloodTestFullViewModel> {

  private BloodTestFullViewModel expected;

  public BloodTestFullViewModelMatcher(BloodTestFullViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A BloodTestFullViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nTest name: ").appendValue(expected.getTestName())
        .appendText("\nTest name short: ").appendValue(expected.getTestNameShort())
        .appendText("\nValid results: ").appendValue(expected.getValidResults())
        .appendText("\nNegative results: ").appendValue(expected.getNegativeResults())
        .appendText("\nPositive results: ").appendValue(expected.getPositiveResults())
        .appendText("\nCategory: ").appendValue(expected.getCategory())
        .appendText("\nBlood test type: ").appendValue(expected.getBloodTestType())
        .appendText("\nRank in category: ").appendValue(expected.getRankInCategory())
        .appendText("\nActive: ").appendValue(expected.getIsActive())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nFlag as unsafe: ").appendValue(expected.isFlagComponentsForDiscard())
        .appendText("\nFlag as unsafe if contains plasma: ").appendValue(expected.isFlagComponentsContainingPlasmaForDiscard());
  }

  @Override
  protected boolean matchesSafely(BloodTestFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getTestName(), expected.getTestName())
        && Objects.equals(actual.getTestNameShort(), expected.getTestNameShort())
        && Objects.equals(actual.getValidResults(), expected.getValidResults())
        && Objects.equals(actual.getNegativeResults(), expected.getNegativeResults())
        && Objects.equals(actual.getPositiveResults(), expected.getPositiveResults())
        && Objects.equals(actual.getCategory(), expected.getCategory())
        && Objects.equals(actual.getBloodTestType(), expected.getBloodTestType())
        && Objects.equals(actual.getRankInCategory(), expected.getRankInCategory())
        && Objects.equals(actual.getIsActive(), expected.getIsActive())
        && Objects.equals(actual.getIsDeleted(), expected.getIsDeleted())
        && Objects.equals(actual.isFlagComponentsForDiscard(), expected.isFlagComponentsForDiscard())
        && Objects.equals(actual.isFlagComponentsContainingPlasmaForDiscard(), expected.isFlagComponentsContainingPlasmaForDiscard());
  }
  
  public static BloodTestFullViewModelMatcher hasSameStateAsBloodTestFullViewModel(BloodTestFullViewModel expected) {
    return new BloodTestFullViewModelMatcher(expected);
  }

}

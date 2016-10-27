package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.BloodTestViewModel;

public class BloodTestViewModelMatcher extends TypeSafeMatcher<BloodTestViewModel> {

  private BloodTestViewModel expected;

  public BloodTestViewModelMatcher(BloodTestViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A BloodTestViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nTest name: ").appendValue(expected.getTestName())
        .appendText("\nTest name short: ").appendValue(expected.getTestNameShort())
        .appendText("\nValid results: ").appendValue(expected.getValidResults())
        .appendText("\nNegative results: ").appendValue(expected.getNegativeResults())
        .appendText("\nPositive results: ").appendValue(expected.getPositiveResults())
        .appendText("\nBlood test category: ").appendValue(expected.getBloodTestCategory())
        .appendText("\nBlood test type: ").appendValue(expected.getBloodTestType())
        .appendText("\nRank in category: ").appendValue(expected.getRankInCategory())
        .appendText("\nActive: ").appendValue(expected.getIsActive())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted());
  }

  @Override
  protected boolean matchesSafely(BloodTestViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getTestName(), expected.getTestName())
        && Objects.equals(actual.getTestNameShort(), expected.getTestNameShort())
        && Objects.equals(actual.getValidResults(), expected.getValidResults())
        && Objects.equals(actual.getNegativeResults(), expected.getNegativeResults())
        && Objects.equals(actual.getPositiveResults(), expected.getPositiveResults())
        && Objects.equals(actual.getBloodTestCategory(), expected.getBloodTestCategory())
        && Objects.equals(actual.getBloodTestType(), expected.getBloodTestType())
        && Objects.equals(actual.getRankInCategory(), expected.getRankInCategory())
        && Objects.equals(actual.getIsActive(), expected.getIsActive())
        && Objects.equals(actual.getIsDeleted(), expected.getIsDeleted());
  }
  
  public static BloodTestViewModelMatcher hasSameStateAsBloodTestViewModel(BloodTestViewModel expected) {
    return new BloodTestViewModelMatcher(expected);
  }

}

package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.BloodTestingRuleResult;

public class BloodTestingRuleResultMatcher extends TypeSafeMatcher<BloodTestingRuleResult> {

  private BloodTestingRuleResult expected;

  public BloodTestingRuleResultMatcher(BloodTestingRuleResult expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A BloodTestingRuleResult with the following state:")
      .appendText("\nTitre: ").appendValue(expected.getTitre());
  }

  @Override
  public boolean matchesSafely(BloodTestingRuleResult actual) {
    return Objects.equals(actual.getTitre(), expected.getTitre());
  }

  public static BloodTestingRuleResultMatcher hasSameStateAsBloodTestingRuleResult(BloodTestingRuleResult expected) {
    return new BloodTestingRuleResultMatcher(expected);
  }

}

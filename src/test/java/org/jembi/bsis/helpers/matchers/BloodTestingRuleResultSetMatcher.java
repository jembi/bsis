package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.repository.bloodtesting.BloodTestingRuleResultSet;

public class BloodTestingRuleResultSetMatcher extends TypeSafeMatcher<BloodTestingRuleResultSet> {

  private BloodTestingRuleResultSet expected;

  public BloodTestingRuleResultSetMatcher(BloodTestingRuleResultSet expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A BloodTestingRuleResultSet with the following state:")
        .appendText("\nTtiStatus: ").appendValue(expected.getTtiStatus());
  }

  @Override
  public boolean matchesSafely(BloodTestingRuleResultSet actual) {
    return Objects.equals(actual.getTtiStatus(), expected.getTtiStatus());
  }

  public static BloodTestingRuleResultSetMatcher hasSameStateAsBloodTestingRuleResultSet(BloodTestingRuleResultSet expected) {
    return new BloodTestingRuleResultSetMatcher(expected);
  }

}
package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRuleResultSet;

public class BloodTestingRuleResultSetMatcher extends TypeSafeMatcher<BloodTestingRuleResultSet> {

  private BloodTestingRuleResultSet expected;

  public BloodTestingRuleResultSetMatcher(BloodTestingRuleResultSet expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A BloodTestingRuleResultSet with the following state:")
        .appendText("\nTtiStatus: ").appendValue(expected.getTtiStatus())
        .appendText("\nBloodTypingStatus: ").appendValue(expected.getBloodTypingStatus())
        .appendText("\nBloodTypingMatchStatus: ").appendValue(expected.getBloodTypingMatchStatus())
        .appendText("\nTitreChanges: ").appendValue(expected.getTitreChanges())
        .appendText("\nPendingAboTestsIds: ").appendValue(expected.getPendingAboTestsIds())
        .appendText("\nPendingRhTestsIds: ").appendValue(expected.getPendingRhTestsIds());
    
    if (!expected.getBloodAboChanges().isEmpty()) {
      description.appendText("\nABO: ").appendValue(expected.getBloodAboChanges().iterator().next());
    }
    
    if (!expected.getBloodRhChanges().isEmpty()) {
      description.appendText("\nRh: ").appendValue(expected.getBloodRhChanges().iterator().next());
    }

  }

  @Override
  public boolean matchesSafely(BloodTestingRuleResultSet actual) {
    
    String actualAbo = actual.getBloodAboChanges().isEmpty() ? null : actual.getBloodAboChanges().iterator().next();
    String actualRh = actual.getBloodRhChanges().isEmpty() ? null : actual.getBloodRhChanges().iterator().next();
    String expectedAbo = expected.getBloodAboChanges().isEmpty() ? null : expected.getBloodAboChanges().iterator().next();
    String expectedRh = expected.getBloodRhChanges().isEmpty() ? null : expected.getBloodRhChanges().iterator().next();
    
    return Objects.equals(actual.getTtiStatus(), expected.getTtiStatus())
        && Objects.equals(actual.getBloodTypingStatus(), expected.getBloodTypingStatus())
        && Objects.equals(actual.getBloodTypingMatchStatus(), expected.getBloodTypingMatchStatus())
        && Objects.equals(actual.getTitreChanges(), expected.getTitreChanges())
        && Objects.equals(actual.getPendingAboTestsIds(), expected.getPendingAboTestsIds())
        && Objects.equals(actual.getPendingRhTestsIds(), expected.getPendingRhTestsIds())
        && Objects.equals(actualAbo, expectedAbo)
        && Objects.equals(actualRh, expectedRh);
  }

  public static BloodTestingRuleResultSetMatcher hasSameStateAsBloodTestingRuleResultSet(BloodTestingRuleResultSet expected) {
    return new BloodTestingRuleResultSetMatcher(expected);
  }

}

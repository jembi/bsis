package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.bloodtesting.BloodTestResult;

public class BloodTestResultMatcher extends TypeSafeMatcher<BloodTestResult> {

  private BloodTestResult expected;

  public BloodTestResultMatcher(BloodTestResult expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A BloodTestResult with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nisDeleted: ").appendValue(expected.getIsDeleted());
  }

  @Override
  protected boolean matchesSafely(BloodTestResult actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getIsDeleted(), expected.getIsDeleted());
  }
  
  public static BloodTestResultMatcher hasSameStateAsBloodTestResult(BloodTestResult expected) {
    return new BloodTestResultMatcher(expected);
  }

}

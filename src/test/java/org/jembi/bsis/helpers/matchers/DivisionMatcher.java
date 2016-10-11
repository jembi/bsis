package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.location.Division;

public class DivisionMatcher extends TypeSafeMatcher<Division> {

  private Division expected;

  public DivisionMatcher(Division expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A Division with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nLevel: ").appendValue(expected.getLevel())
        .appendText("\nParent: ").appendValue(expected.getParent());
  }

  @Override
  protected boolean matchesSafely(Division actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName(), expected.getName())
        && Objects.equals(actual.getLevel(), expected.getLevel())
        && Objects.equals(actual.getParent(), expected.getParent());
  }
  
  public static DivisionMatcher hasSameStateAsDivision(Division expected) {
    return new DivisionMatcher(expected);
  }

}
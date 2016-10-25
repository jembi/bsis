package org.jembi.bsis.helpers.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.componenttype.ComponentTypeCombination;
import java.util.Objects;

public class ComponentTypeCombinationMatcher extends TypeSafeMatcher<ComponentTypeCombination> {

  private ComponentTypeCombination expected;

  public ComponentTypeCombinationMatcher(ComponentTypeCombination expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    System.out.println(description.appendText("Component Type Combination form entity with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nCombination Name: ").appendValue(expected.getCombinationName())
        .appendText("\nComponent Types: ").appendValue(expected.getComponentTypes())
        .appendText("\nSource Component Types: ").appendValue(expected.getSourceComponentTypes()));
  }
  
  @Override
  public boolean matchesSafely(ComponentTypeCombination actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getCombinationName(), expected.getCombinationName()) &&
        Objects.equals(actual.getComponentTypes(), expected.getComponentTypes()) &&
        Objects.equals(actual.getSourceComponentTypes(), expected.getSourceComponentTypes()) &&
        Objects.equals(actual.getIsDeleted(),  expected.getIsDeleted());
  }

  public static ComponentTypeCombinationMatcher hasSameStateAsComponentTypeCombination(ComponentTypeCombination expected) {
    return new ComponentTypeCombinationMatcher(expected);
  }
}

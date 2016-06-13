package helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import model.component.Component;

public class ComponentMatcher extends TypeSafeMatcher<Component> {

  private Component expected;

  public ComponentMatcher(Component expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A donor with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nStatus: ").appendValue(expected.getStatus())
        .appendText("\nInventoryStatus: ").appendValue(expected.getInventoryStatus())
        .appendText("\nLocation: ").appendValue(expected.getLocation());
  }

  @Override
  public boolean matchesSafely(Component actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getStatus(), expected.getStatus()) &&
        Objects.equals(actual.getInventoryStatus(), expected.getInventoryStatus()) &&
        Objects.equals(actual.getLocation(), expected.getLocation());
  }

  public static ComponentMatcher hasSameStateAsComponent(Component expected) {
    return new ComponentMatcher(expected);
  }

}

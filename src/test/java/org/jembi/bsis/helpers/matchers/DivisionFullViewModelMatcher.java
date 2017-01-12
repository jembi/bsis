package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.DivisionFullViewModel;

public class DivisionFullViewModelMatcher extends TypeSafeMatcher<DivisionFullViewModel> {

  private DivisionFullViewModel expected;

  public DivisionFullViewModelMatcher(DivisionFullViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A DivisionFullViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nLevel: ").appendValue(expected.getLevel())
        .appendText("\nParent: ").appendValue(expected.getParent())
        .appendText("\nPermissions: ").appendValue(expected.getPermissions());
  }

  @Override
  protected boolean matchesSafely(DivisionFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName(), expected.getName())
        && Objects.equals(actual.getLevel(), expected.getLevel())
        && Objects.equals(actual.getParent(), expected.getParent())
        && Objects.equals(actual.getPermissions(), expected.getPermissions());
  }
  
  public static DivisionFullViewModelMatcher hasSameStateAsDivisionFullViewModel(DivisionFullViewModel expected) {
    return new DivisionFullViewModelMatcher(expected);
  }

}

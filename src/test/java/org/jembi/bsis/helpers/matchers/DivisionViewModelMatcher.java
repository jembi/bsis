package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.DivisionViewModel;

public class DivisionViewModelMatcher extends TypeSafeMatcher<DivisionViewModel> {

  private DivisionViewModel expected;

  public DivisionViewModelMatcher(DivisionViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A DivisionViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nLevel: ").appendValue(expected.getLevel())
        .appendText("\nParent: ").appendValue(expected.getParent());
  }

  @Override
  protected boolean matchesSafely(DivisionViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName(), expected.getName())
        && Objects.equals(actual.getLevel(), expected.getLevel())
        && matchParent(actual.getParent(), expected.getParent());
  }
  
  // This method was specifically created so that java will not use 
  // default equals method when comparing the object parent which 
  // will always return false as a new object is always created
  private boolean matchParent(DivisionViewModel actualParent, DivisionViewModel expectedParent) {
    if (actualParent == null && expectedParent == null) {
      return true;
    } else if ((actualParent == null && expectedParent != null) || (actualParent != null && expectedParent == null)) {
      return false;
    }
    return Objects.equals(actualParent.getId(), expectedParent.getId())
        && Objects.equals(actualParent.getName(), expectedParent.getName())
        && Objects.equals(actualParent.getLevel(), expectedParent.getLevel());
  }
  
  public static DivisionViewModelMatcher hasSameStateAsDivisionViewModel(DivisionViewModel expected) {
    return new DivisionViewModelMatcher(expected);
  }

}

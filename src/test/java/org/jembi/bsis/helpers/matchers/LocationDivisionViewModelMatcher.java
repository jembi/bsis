package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.LocationDivisionViewModel;

public class LocationDivisionViewModelMatcher extends TypeSafeMatcher<LocationDivisionViewModel> {

  private LocationDivisionViewModel expected;

  public LocationDivisionViewModelMatcher(LocationDivisionViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A LocationDivisionViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nLevel: ").appendValue(expected.getLevel());
  }

  @Override
  protected boolean matchesSafely(LocationDivisionViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName(), expected.getName())
        && Objects.equals(actual.getLevel(), expected.getLevel());
  }
  
  public static LocationDivisionViewModelMatcher hasSameStateAsLocationDivisionViewModel(
      LocationDivisionViewModel expected) {
    return new LocationDivisionViewModelMatcher(expected);
  }

}

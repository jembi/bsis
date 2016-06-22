package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.location.Location;

public class LocationMatcher extends TypeSafeMatcher<Location> {

  private Location expected;

  public LocationMatcher(Location expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A location with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nisUsageSite: ").appendValue(expected.getIsUsageSite())
        .appendText("\nisMobileSite: ").appendValue(expected.getIsMobileSite())
        .appendText("\nisVenue: ").appendValue(expected.getIsVenue())
        .appendText("\nisDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nNotes: ").appendValue(expected.getNotes());
  }

  @Override
  protected boolean matchesSafely(Location actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName(), expected.getName())
        && Objects.equals(actual.getIsUsageSite(), expected.getIsUsageSite())
        && Objects.equals(actual.getIsMobileSite(), expected.getIsMobileSite())
        && Objects.equals(actual.getIsVenue(), expected.getIsVenue())
        && Objects.equals(actual.getIsDeleted(), expected.getIsDeleted())
        && Objects.equals(actual.getNotes(), expected.getNotes());
  }
  
  public static LocationMatcher hasSameStateAsLocation(Location expected) {
    return new LocationMatcher(expected);
  }

}

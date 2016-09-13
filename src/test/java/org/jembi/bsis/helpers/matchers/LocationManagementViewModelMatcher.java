package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.LocationManagementViewModel;


public class LocationManagementViewModelMatcher extends TypeSafeMatcher<LocationManagementViewModel> {

  private LocationManagementViewModel expected;

  public LocationManagementViewModelMatcher(LocationManagementViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A LocationManagementViewModel with the following state:")
         .appendText("\nId: ").appendValue(expected.getId())
         .appendText("\nName: ").appendValue(expected.getName())
         .appendText("\nIsDeleted: ").appendValue(expected.getIsDeleted())
         .appendText("\nDivisionLevel3: ").appendValue(expected.getDivisionLevel3());
  }

  @Override
  protected boolean matchesSafely(LocationManagementViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) 
        && Objects.equals(actual.getName(), expected.getName())
        && Objects.equals(actual.getIsDeleted(), expected.getIsDeleted())
        && Objects.equals(actual.getDivisionLevel3(), expected.getDivisionLevel3());
  }

  public static LocationManagementViewModelMatcher hasSameStateAsLocationManagementViewModel(
      LocationManagementViewModel expected) {
    return new LocationManagementViewModelMatcher(expected);
  }
}

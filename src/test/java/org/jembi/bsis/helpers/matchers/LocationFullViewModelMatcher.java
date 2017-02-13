package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.LocationFullViewModel;

public class LocationFullViewModelMatcher extends TypeSafeMatcher<LocationFullViewModel> {

  private LocationFullViewModel expected;

  public LocationFullViewModelMatcher(LocationFullViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A LocationFullViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nName: ").appendValue(expected.getName())
        .appendText("\nisUsageSite: ").appendValue(expected.getIsUsageSite())
        .appendText("\nisMobileSite: ").appendValue(expected.getIsMobileSite())
        .appendText("\nisVenue: ").appendValue(expected.getIsVenue())
        .appendText("\nisDistributionSite: ").appendValue(expected.getIsDistributionSite())
        .appendText("\nisProcessingSite: ").appendValue(expected.getIsProcessingSite())
        .appendText("\nisTestingSite: ").appendValue(expected.getIsTestingSite())
        .appendText("\nisReferralSite: ").appendValue(expected.getIsReferralSite())
        .appendText("\nisDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nDivision level 1: ").appendValue(expected.getDivisionLevel1())
        .appendText("\nDivision level 2: ").appendValue(expected.getDivisionLevel2())
        .appendText("\nDivision level 3: ").appendValue(expected.getDivisionLevel3());
  }

  @Override
  protected boolean matchesSafely(LocationFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getName(), expected.getName())
        && Objects.equals(actual.getIsUsageSite(), expected.getIsUsageSite())
        && Objects.equals(actual.getIsMobileSite(), expected.getIsMobileSite())
        && Objects.equals(actual.getIsVenue(), expected.getIsVenue())
        && Objects.equals(actual.getIsDistributionSite(), expected.getIsDistributionSite())
        && Objects.equals(actual.getIsProcessingSite(), expected.getIsProcessingSite())
        && Objects.equals(actual.getIsTestingSite(), expected.getIsTestingSite())
        && Objects.equals(actual.getIsReferralSite(), expected.getIsReferralSite())
        && Objects.equals(actual.getIsDeleted(), expected.getIsDeleted())
        && Objects.equals(actual.getDivisionLevel1(), expected.getDivisionLevel1())
        && Objects.equals(actual.getDivisionLevel2(), expected.getDivisionLevel2())
        && Objects.equals(actual.getDivisionLevel3(), expected.getDivisionLevel3());
  }
  
  public static LocationFullViewModelMatcher hasSameStateAsLocationFullViewModel(LocationFullViewModel expected) {
    return new LocationFullViewModelMatcher(expected);
  }

}

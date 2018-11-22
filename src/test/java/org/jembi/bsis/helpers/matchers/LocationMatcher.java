package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.model.location.Location;

public class LocationMatcher extends AbstractTypeSafeMatcher<Location> {

  public LocationMatcher(Location expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, Location location) {
    description.appendText("A location with the following state:")
        .appendText("\nId: ").appendValue(location.getId())
        .appendText("\nName: ").appendValue(location.getName())
        .appendText("\nisUsageSite: ").appendValue(location.getIsUsageSite())
        .appendText("\nisMobileSite: ").appendValue(location.getIsMobileSite())
        .appendText("\nisVenue: ").appendValue(location.getIsVenue())
        .appendText("\nisDistributionSite: ").appendValue(location.getIsDistributionSite())
        .appendText("\nisProcessingSite: ").appendValue(location.getIsProcessingSite())
        .appendText("\nisTestingSite: ").appendValue(location.getIsTestingSite())
        .appendText("\nisReferralSite: ").appendValue(location.getIsReferralSite())
        .appendText("\nisDeleted: ").appendValue(location.getIsDeleted())
        .appendText("\nNotes: ").appendValue(location.getNotes())
        .appendText("\nDivision level 1: ").appendValue(location.getDivisionLevel1())
        .appendText("\nDivision level 2: ").appendValue(location.getDivisionLevel2())
        .appendText("\nDivision level 3: ").appendValue(location.getDivisionLevel3());
  }

  @Override
  protected boolean matchesSafely(Location actual) {
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
        && Objects.equals(actual.getNotes(), expected.getNotes())
        && Objects.equals(actual.getDivisionLevel1(), expected.getDivisionLevel1())
        && Objects.equals(actual.getDivisionLevel2(), expected.getDivisionLevel2())
        && Objects.equals(actual.getDivisionLevel3(), expected.getDivisionLevel3());
  }
  
  public static LocationMatcher hasSameStateAsLocation(Location expected) {
    return new LocationMatcher(expected);
  }

}

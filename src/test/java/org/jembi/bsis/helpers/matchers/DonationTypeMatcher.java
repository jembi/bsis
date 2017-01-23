package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.donationtype.DonationType;

public class DonationTypeMatcher extends TypeSafeMatcher<DonationType> {

  private DonationType expected;

  public DonationTypeMatcher(DonationType expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A donation type with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nDonation type: ").appendValue(expected.getDonationType())
        .appendText("\nIs deleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nCreated date").appendValue(expected.getCreatedDate())
        .appendText("\nCreated by").appendValue(expected.getCreatedBy())
        .appendText("\nLast updated").appendValue(expected.getLastUpdated())
        .appendText("\nLast updated by").appendValue(expected.getLastUpdatedBy());
  }

  @Override
  public boolean matchesSafely(DonationType actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getDonationType(), expected.getDonationType()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted()) &&
        Objects.equals(actual.getCreatedDate(), expected.getCreatedDate()) &&
        Objects.equals(actual.getCreatedBy(), expected.getCreatedBy()) &&
        Objects.equals(actual.getLastUpdated(), expected.getLastUpdated()) &&
        Objects.equals(actual.getLastUpdatedBy(), expected.getLastUpdatedBy());
  }

  public static DonationTypeMatcher hasSameStateAsDonationType(DonationType expected) {
    return new DonationTypeMatcher(expected);
  }
}
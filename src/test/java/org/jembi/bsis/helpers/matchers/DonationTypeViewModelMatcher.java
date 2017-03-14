package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.DonationTypeViewModel;

public class DonationTypeViewModelMatcher extends TypeSafeMatcher<DonationTypeViewModel> {

  private DonationTypeViewModel expected;

  public DonationTypeViewModelMatcher(DonationTypeViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A donation type view model with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nDonation type: ").appendValue(expected.getType())
        .appendText("\nIs deleted: ").appendValue(expected.getIsDeleted());
  }

  @Override
  public boolean matchesSafely(DonationTypeViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getType(), expected.getType()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted());
  }

  public static DonationTypeViewModelMatcher hasSameStateAsDonationTypeViewModel(DonationTypeViewModel expected) {
    return new DonationTypeViewModelMatcher(expected);
  }
}
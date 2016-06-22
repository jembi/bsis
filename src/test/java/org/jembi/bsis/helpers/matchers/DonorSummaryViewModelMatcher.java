package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.DonorSummaryViewModel;

public class DonorSummaryViewModelMatcher extends TypeSafeMatcher<DonorSummaryViewModel> {

  private DonorSummaryViewModel expected;

  public DonorSummaryViewModelMatcher(DonorSummaryViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A donor summary view model with the following state:")
        .appendText("\nBirth date: ").appendValue(expected.getBirthDate())
        .appendText("\nFirst name: ").appendValue(expected.getFirstName())
        .appendText("\nLast name: ").appendValue(expected.getLastName())
        .appendText("\nGender: ").appendValue(expected.getGender())
        .appendText("\nDonor number: ").appendValue(expected.getDonorNumber())
        .appendText("\nVenue name: ").appendValue(expected.getVenueName());
  }

  @Override
  public boolean matchesSafely(DonorSummaryViewModel actual) {
    return Objects.equals(actual.getBirthDate(), expected.getBirthDate())
        && Objects.equals(actual.getFirstName(), expected.getFirstName())
        && Objects.equals(actual.getLastName(), expected.getLastName())
        && Objects.equals(actual.getGender(), expected.getGender())
        && Objects.equals(actual.getDonorNumber(), expected.getDonorNumber())
        && Objects.equals(actual.getVenueName(), expected.getVenueName());
  }

  public static DonorSummaryViewModelMatcher hasSameStateAsDonorSummaryViewModel(DonorSummaryViewModel expected) {
    return new DonorSummaryViewModelMatcher(expected);
  }

}

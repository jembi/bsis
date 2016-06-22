package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.DonorViewModel;

public class DonorViewModelMatcher extends TypeSafeMatcher<DonorViewModel> {

  private DonorViewModel expected;

  public DonorViewModelMatcher(DonorViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A donor view model with the following state:")
        .appendText("\nDonor: ").appendValue(expected.getDonor())
        .appendText("\nPermissions: ").appendValue(expected.getPermissions());
  }

  @Override
  public boolean matchesSafely(DonorViewModel actual) {
    return Objects.equals(actual.getDonor(), expected.getDonor()) &&
        Objects.equals(actual.getPermissions(), expected.getPermissions());
  }

  public static DonorViewModelMatcher hasSameStateAsDonorViewModel(DonorViewModel expected) {
    return new DonorViewModelMatcher(expected);
  }

}

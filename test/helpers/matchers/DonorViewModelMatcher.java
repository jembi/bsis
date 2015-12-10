package helpers.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import viewmodel.DonorViewModel;

import java.util.Objects;

public class DonorViewModelMatcher extends TypeSafeMatcher<DonorViewModel> {

  private DonorViewModel expected;

  public DonorViewModelMatcher(DonorViewModel expected) {
    this.expected = expected;
  }

  public static DonorViewModelMatcher hasSameStateAsDonorViewModel(DonorViewModel expected) {
    return new DonorViewModelMatcher(expected);
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

}

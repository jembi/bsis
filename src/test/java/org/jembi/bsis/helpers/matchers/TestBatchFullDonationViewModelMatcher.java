package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.TestBatchFullDonationViewModel;

public class TestBatchFullDonationViewModelMatcher extends TypeSafeMatcher<TestBatchFullDonationViewModel>  {

  private TestBatchFullDonationViewModel expected;

  public TestBatchFullDonationViewModelMatcher(TestBatchFullDonationViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A test batch full donation view model with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nTest batch date: ").appendValue(expected.getTestBatchDate())
        .appendText("\nDonations: ").appendValue(expected.getDonations());
  }

  @Override
  protected void describeMismatchSafely(TestBatchFullDonationViewModel actual, Description description) {

    description.appendText("The following fields did not match:");

    if (!Objects.equals(actual.getId(), expected.getId())) {
      description.appendText("\nId: expected = ").appendValue(expected.getId())
          .appendText(", actual = ").appendValue(actual.getId());
    }

    if (!Objects.equals(actual.getTestBatchDate(), expected.getTestBatchDate())) {
      description.appendText("\nTest batch date: expected = ").appendValue(expected.getTestBatchDate())
          .appendText(", actual = ").appendValue(actual.getTestBatchDate());
    }

    if (!Objects.equals(actual.getDonations(), expected.getDonations())) {
      description.appendText("\nDonations: expected = ").appendValue(expected.getDonations())
          .appendText(", actual = ").appendValue(actual.getDonations());
    }
  }

  @Override
  public boolean matchesSafely(TestBatchFullDonationViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getTestBatchDate(), expected.getTestBatchDate()) &&
        Objects.equals(actual.getDonations(), expected.getDonations());
  }

  public static TestBatchFullDonationViewModelMatcher hasSameStateAsTestBatchFullDonationViewModel(TestBatchFullDonationViewModel expected) {
    return new TestBatchFullDonationViewModelMatcher(expected);
  }

}

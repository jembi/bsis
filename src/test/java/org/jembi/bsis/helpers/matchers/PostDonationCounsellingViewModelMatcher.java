package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.PostDonationCounsellingViewModel;


public class PostDonationCounsellingViewModelMatcher extends TypeSafeMatcher<PostDonationCounsellingViewModel> {

  private PostDonationCounsellingViewModel expected;

  public PostDonationCounsellingViewModelMatcher(PostDonationCounsellingViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A PostDonationCounselling view model with the following state:")
        .appendText("\nDonation: ").appendValue(expected.getDonation())
        .appendText("\nPermissions: ").appendValue(expected.getPermissions())
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nFlaggedForCounselling: ").appendValue(expected.isFlaggedForCounselling())
        .appendText("\nCounsellingDate: ").appendValue(expected.getCounsellingDate())
        .appendText("\nNotes: ").appendValue(expected.getNotes());
  }

  @Override
  public boolean matchesSafely(PostDonationCounsellingViewModel actual) {
    return Objects.equals(actual.getDonation(), expected.getDonation()) &&
        Objects.equals(actual.getPermissions(), expected.getPermissions()) &&
        Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.isFlaggedForCounselling(), expected.isFlaggedForCounselling()) &&
        Objects.equals(actual.getCounsellingDate(), expected.getCounsellingDate()) &&
        Objects.equals(actual.getNotes(), expected.getNotes());
  }

  public static PostDonationCounsellingViewModelMatcher hasSameStateAsPostDonationCounsellingViewModel(PostDonationCounsellingViewModel expected) {
    return new PostDonationCounsellingViewModelMatcher(expected);
  }
}

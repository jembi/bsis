package helpers.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import viewmodel.PostDonationCounsellingViewModel;

import java.util.Objects;


public class PostDonationCounsellingViewModelMatcher extends TypeSafeMatcher<PostDonationCounsellingViewModel> {

  private PostDonationCounsellingViewModel expected;

  public PostDonationCounsellingViewModelMatcher(PostDonationCounsellingViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A PostDonationCounselling view model with the following state:")
        .appendText("\nPostDonationCounselling: ").appendValue(expected.getPostDonationCounselling())
        .appendText("\nPermissions: ").appendValue(expected.getPermissions());
  }

  @Override
  public boolean matchesSafely(PostDonationCounsellingViewModel actual) {
    return Objects.equals(actual.getPostDonationCounselling(), expected.getPostDonationCounselling()) &&
        Objects.equals(actual.getPermissions(), expected.getPermissions());
  }

  public static PostDonationCounsellingViewModelMatcher hasSameStateAsPostDonationCounsellingViewModel(PostDonationCounsellingViewModel expected) {
    return new PostDonationCounsellingViewModelMatcher(expected);
  }
}

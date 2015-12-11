package helpers.matchers;

import model.donordeferral.DonorDeferral;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Objects;

public class DonorDeferralMatcher extends TypeSafeMatcher<DonorDeferral> {

  private DonorDeferral expected;

  private DonorDeferralMatcher(DonorDeferral expected) {
    this.expected = expected;
  }

  public static DonorDeferralMatcher hasSameStateAsDonorDeferral(DonorDeferral expected) {
    return new DonorDeferralMatcher(expected);
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A donor deferral with the following state:")
            .appendText("\nDonor: ").appendValue(expected.getDeferredDonor())
            .appendText("\nDeferral reason: ").appendValue(expected.getDeferralReason())
            .appendText("\nDeferred until: ").appendValue(expected.getDeferredUntil())
            .appendText("\nVoided: ").appendValue(expected.getIsVoided());
  }

  @Override
  public boolean matchesSafely(DonorDeferral actual) {
    return Objects.equals(actual.getDeferredDonor(), expected.getDeferredDonor()) &&
            Objects.equals(actual.getDeferralReason(), expected.getDeferralReason()) &&
            Objects.equals(actual.getDeferredUntil(), expected.getDeferredUntil()) &&
            Objects.equals(actual.getIsVoided(), expected.getIsVoided());
  }

}

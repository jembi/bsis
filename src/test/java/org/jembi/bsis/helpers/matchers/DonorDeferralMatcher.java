package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.donordeferral.DonorDeferral;

public class DonorDeferralMatcher extends TypeSafeMatcher<DonorDeferral> {

  private DonorDeferral expected;

  public DonorDeferralMatcher(DonorDeferral expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A donor deferral with the following state:")
        .appendText("\nDonor: ").appendValue(expected.getDeferredDonor())
        .appendText("\nDeferral reason: ").appendValue(expected.getDeferralReason())
        .appendText("\nDeferred until: ").appendValue(expected.getDeferredUntil())
        .appendText("\nDeferral date: ").appendValue(expected.getDeferralDate())
        .appendText("\nVenue: ").appendValue(expected.getVenue())
        .appendText("\nVoided: ").appendValue(expected.getIsVoided());
  }

  @Override
  public boolean matchesSafely(DonorDeferral actual) {
    return Objects.equals(actual.getDeferredDonor(), expected.getDeferredDonor()) &&
        Objects.equals(actual.getDeferralReason(), expected.getDeferralReason()) &&
        Objects.equals(actual.getDeferredUntil(), expected.getDeferredUntil()) &&
        Objects.equals(actual.getDeferralDate(), expected.getDeferralDate()) &&
        Objects.equals(actual.getVenue(), expected.getVenue()) &&
        Objects.equals(actual.getIsVoided(), expected.getIsVoided());
  }

  public static DonorDeferralMatcher hasSameStateAsDonorDeferral(DonorDeferral expected) {
    return new DonorDeferralMatcher(expected);
  }

}
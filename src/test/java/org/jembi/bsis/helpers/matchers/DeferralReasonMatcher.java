package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.DeferralReasonViewModel;

public class DeferralReasonMatcher extends TypeSafeMatcher<DeferralReasonViewModel> {

  private DeferralReasonViewModel expected;

  public DeferralReasonMatcher(DeferralReasonViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A deferral reason with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nReason: ").appendValue(expected.getReason())
        .appendText("\nDefault duration: ").appendValue(expected.getDefaultDuration())
        .appendText("\nDuration type: ").appendValue(expected.getDurationType())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted());
  }

  @Override
  public boolean matchesSafely(DeferralReasonViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getReason(), expected.getReason()) &&
        Objects.equals(actual.getDefaultDuration(), expected.getDefaultDuration()) &&
        Objects.equals(actual.getDurationType(), expected.getDurationType()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted());
  }

  public static DeferralReasonMatcher hasSameStateAsDeferralReasonViewModel(DeferralReasonViewModel expected) {
    return new DeferralReasonMatcher(expected);
  }

}
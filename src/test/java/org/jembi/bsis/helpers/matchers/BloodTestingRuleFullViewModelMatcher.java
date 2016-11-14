package org.jembi.bsis.helpers.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.BloodTestingRuleFullViewModel;

import java.util.Objects;

public class BloodTestingRuleFullViewModelMatcher extends TypeSafeMatcher<BloodTestingRuleFullViewModel> {

  private BloodTestingRuleFullViewModel expected;

  public BloodTestingRuleFullViewModelMatcher (BloodTestingRuleFullViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("Blood testing rule full view model with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nTest name short: ").appendValue(expected.getTestNameShort())
        .appendText("\nDonation field changed: ").appendValue(expected.getDonationFieldChanged())
        .appendText("\nCategory: ").appendValue(expected.getCategory())
        .appendText("\nNew information: ").appendValue(expected.getNewInformation())
        .appendText("\nPattern: ").appendValue(expected.getPattern())
        .appendText("\nBloodTest: ").appendValue(expected.getBloodTest())
        .appendText("\nPending Tests: ").appendValue(expected.getPendingTests())
        .appendText("\nIs deleted: ").appendValue(expected.getIsDeleted());
  }

  @Override
  public boolean matchesSafely(BloodTestingRuleFullViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getTestNameShort(), expected.getTestNameShort()) &&
        Objects.equals(actual.getDonationFieldChanged(), expected.getDonationFieldChanged()) &&
        Objects.equals(actual.getCategory(), expected.getCategory()) &&
        Objects.equals(actual.getPattern(), expected.getPattern()) &&
        Objects.equals(actual.getNewInformation(), expected.getNewInformation()) &&
        Objects.equals(actual.getBloodTest(), expected.getBloodTest()) &&
        Objects.equals(actual.getPendingTests(), expected.getPendingTests()) &&
        Objects.equals(actual.getIsDeleted(), expected.getIsDeleted());
  }

  public static BloodTestingRuleFullViewModelMatcher hasSameStateAsBloodTestingRuleFullViewModel(BloodTestingRuleFullViewModel expected) {
    return new BloodTestingRuleFullViewModelMatcher(expected);
  }
}

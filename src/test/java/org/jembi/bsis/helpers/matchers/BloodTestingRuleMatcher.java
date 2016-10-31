package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;

public class BloodTestingRuleMatcher extends TypeSafeMatcher<BloodTestingRule> {

  private BloodTestingRule expected;

  public BloodTestingRuleMatcher(BloodTestingRule expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A BloodTestingRule with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nBlood test ids: ").appendValue(expected.getBloodTestsIds())
        .appendText("\nCategory: ").appendValue(expected.getCategory())
        .appendText("\nSub category: ").appendValue(expected.getSubCategory())
        .appendText("\nDonation field changed: ").appendValue(expected.getDonationFieldChanged())
        .appendText("\nNew information: ").appendValue(expected.getNewInformation())
        .appendText("\nPattern: ").appendValue(expected.getPattern())
        .appendText("\nPending tests ids: ").appendValue(expected.getPendingTestsIds())
        .appendText("\nIs active: ").appendValue(expected.getIsActive());
  }

  @Override
  protected boolean matchesSafely(BloodTestingRule actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getBloodTestsIds(), expected.getBloodTestsIds())
        && Objects.equals(actual.getCategory(), expected.getCategory())
        && Objects.equals(actual.getSubCategory(), expected.getSubCategory())
        && Objects.equals(actual.getDonationFieldChanged(), expected.getDonationFieldChanged())
        && Objects.equals(actual.getNewInformation(), expected.getNewInformation())
        && Objects.equals(actual.getPattern(), expected.getPattern())
        && Objects.equals(actual.getPendingTestsIds(), expected.getPendingTestsIds())
        && Objects.equals(actual.getIsActive(), expected.getIsActive())
        ;
  }
  
  public static BloodTestingRuleMatcher hasSameStateAsBloodTestingRule(BloodTestingRule expected) {
    return new BloodTestingRuleMatcher(expected);
  }

}

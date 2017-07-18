package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.model.bloodtesting.rules.BloodTestingRule;

public class BloodTestingRuleMatcher extends AbstractTypeSafeMatcher<BloodTestingRule> {

  public BloodTestingRuleMatcher(BloodTestingRule expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, BloodTestingRule entity) {
    description.appendText("A BloodTestingRule with the following state:")
        .appendText("\nId: ").appendValue(entity.getId())
        .appendText("\nBlood test id: ").appendValue(entity.getBloodTest().getId())
        .appendText("\nDonation field changed: ").appendValue(entity.getDonationFieldChanged())
        .appendText("\nNew information: ").appendValue(entity.getNewInformation())
        .appendText("\nPattern: ").appendValue(entity.getPattern())
        .appendText("\nPending tests ids: ").appendValue(entity.getPendingTestsIdsSet())
        .appendText("\nIs deleted: ").appendValue(entity.getIsDeleted());
  }

  @Override
  protected boolean matchesSafely(BloodTestingRule actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getBloodTest(), expected.getBloodTest())
        && Objects.equals(actual.getDonationFieldChanged(), expected.getDonationFieldChanged())
        && Objects.equals(actual.getNewInformation(), expected.getNewInformation())
        && Objects.equals(actual.getPattern(), expected.getPattern())
        && Objects.equals(actual.getPendingTestsIdsSet(), expected.getPendingTestsIdsSet())
        && Objects.equals(actual.getIsDeleted(), expected.getIsDeleted())
        ;
  }
  
  public static BloodTestingRuleMatcher hasSameStateAsBloodTestingRule(BloodTestingRule expected) {
    return new BloodTestingRuleMatcher(expected);
  }

}

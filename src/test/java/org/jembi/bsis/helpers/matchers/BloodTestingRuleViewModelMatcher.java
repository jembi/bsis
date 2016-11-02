package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.BloodTestingRuleViewModel;

public class BloodTestingRuleViewModelMatcher extends TypeSafeMatcher<BloodTestingRuleViewModel> {
  
  private BloodTestingRuleViewModel expected;
  
  public BloodTestingRuleViewModelMatcher(BloodTestingRuleViewModel expected) {
    this.expected = expected;
  }
  
  @Override
  public void describeTo(Description description) {
    System.out.println(description.appendText("Blood testing rule entity with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nBlood Test short name: ").appendValue(expected.getTestNameShort())
        .appendText("\nDonation field: ").appendValue(expected.getDonationFieldChanged())
        .appendText("\nCategory: ").appendValue(expected.getCategory())
        .appendText("\nDonation field value: ").appendValue(expected.getNewInformation())
        .appendText("\nTest outcome: ").appendValue(expected.getPattern())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted()));
  }
  
  @Override
  public boolean matchesSafely(BloodTestingRuleViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getTestNameShort(), expected.getTestNameShort()) &&
        Objects.equals(actual.getDonationFieldChanged(), expected.getDonationFieldChanged()) &&
        Objects.equals(actual.getCategory(), expected.getCategory()) &&
        Objects.equals(actual.getPattern(), expected.getPattern()) &&
        Objects.equals(actual.getIsDeleted(),  expected.getIsDeleted());  
  }
 }

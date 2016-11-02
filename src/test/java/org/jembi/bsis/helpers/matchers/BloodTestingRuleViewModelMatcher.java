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
        .appendText("\nBlood Test short name: ").appendValue(expected.getBloodTestNameShort())
        .appendText("\nDonation field: ").appendValue(expected.getDonationField())
        .appendText("\nCategory: ").appendValue(expected.getCategory())
        .appendText("\nDonation field value: ").appendValue(expected.getDonationFieldValue())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted()));
  }
  
  @Override
  public boolean matchesSafely(BloodTestingRuleViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getBloodTestNameShort(), expected.getBloodTestNameShort()) &&
        Objects.equals(actual.getDonationField(), expected.getDonationField()) &&
        Objects.equals(actual.getCategory(), expected.getCategory()) &&
        Objects.equals(actual.getIsDeleted(),  expected.getIsDeleted());  
  }
 }

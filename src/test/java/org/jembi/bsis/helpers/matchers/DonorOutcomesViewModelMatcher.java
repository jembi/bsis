package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.DonorOutcomesViewModel;

public class DonorOutcomesViewModelMatcher extends TypeSafeMatcher<DonorOutcomesViewModel> {

  private DonorOutcomesViewModel expected;

  public DonorOutcomesViewModelMatcher(DonorOutcomesViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A DonorOutcomesViewModel with the following state:")
        .appendText("\nDonor number: ").appendValue(expected.getDonorNumber())
        .appendText("\nLast name: ").appendValue(expected.getLastName())
        .appendText("\nFirst name: ").appendValue(expected.getFirstName())
        .appendText("\nGender: ").appendValue(expected.getGender())
        .appendText("\nBirth date: ").appendValue(expected.getBirthDate())
        .appendText("\nDonation date: ").appendValue(expected.getDonationDate())
        .appendText("\nDIN: ").appendValue(expected.getDonationIdentificationNumber())
        .appendText("\nBlood ABO: ").appendValue(expected.getBloodAbo())
        .appendText("\nBlood Rh: ").appendValue(expected.getBloodRh())
        .appendText("\nBlood test results: ").appendValue(expected.getBloodTestResults());
  }

  @Override
  protected boolean matchesSafely(DonorOutcomesViewModel actual) {
    return Objects.equals(actual.getDonorNumber(), expected.getDonorNumber())
        && Objects.equals(actual.getLastName(), expected.getLastName())
        && Objects.equals(actual.getFirstName(), expected.getFirstName())
        && Objects.equals(actual.getGender(), expected.getGender())
        && Objects.equals(actual.getBirthDate(), expected.getBirthDate())
        && Objects.equals(actual.getDonationDate(), expected.getDonationDate())
        && Objects.equals(actual.getDonationIdentificationNumber(), expected.getDonationIdentificationNumber())
        && Objects.equals(actual.getBloodAbo(), expected.getBloodAbo())
        && Objects.equals(actual.getBloodRh(), expected.getBloodRh())
        && Objects.equals(actual.getBloodTestResults(), expected.getBloodTestResults());
  }
  
  public static DonorOutcomesViewModelMatcher withSameStateAsDonorOutcomesViewModel(DonorOutcomesViewModel expected) {
    return new DonorOutcomesViewModelMatcher(expected);
  }

}

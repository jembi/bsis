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
        .appendText("\nLastName: ").appendValue(expected.getLastName())
        .appendText("\nFirst name: ").appendValue(expected.getFirstName())
        .appendText("\nGender: ").appendValue(expected.getGender())
        .appendText("\nBirthDate: ").appendValue(expected.getBirthDate())
        .appendText("\nDonationDate: ").appendValue(expected.getDonationDate())
        .appendText("\nDonationIdentificationNumber: ").appendValue(expected.getDonationIdentificationNumber())
        .appendText("\nBloodTestResults: ").appendValue(expected.getBloodTestResults());
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
        && Objects.equals(actual.getBloodTestResults(), expected.getBloodTestResults());
  }
  
  public static DonorOutcomesViewModelMatcher withSameStateAsDonorOutcomesViewModel(DonorOutcomesViewModel expected) {
    return new DonorOutcomesViewModelMatcher(expected);
  }

}

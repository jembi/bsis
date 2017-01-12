package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.MobileClinicExportDonorViewModel;

public class MobileClinicExportViewModelMatcher extends TypeSafeMatcher<MobileClinicExportDonorViewModel> {

  private MobileClinicExportDonorViewModel expected;

  public MobileClinicExportViewModelMatcher(MobileClinicExportDonorViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A MobileClinicExportDonorViewModel with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nDonorNumber: ").appendValue(expected.getDonorNumber())
        .appendText("\nFirstName: ").appendValue(expected.getFirstName())
        .appendText("\nLastName: ").appendValue(expected.getLastName())
        .appendText("\nGender: ").appendValue(expected.getGender())
        .appendText("\nBloodType: ").appendValue(expected.getBloodType())
        .appendText("\nDonorStatus").appendValue(expected.getDonorStatus())
        .appendText("\nBirthDate").appendValue(expected.getBirthDate())
        .appendText("\nVenue: ").appendValue(expected.getVenue())
        .appendText("\nDeleted: ").appendValue(expected.getIsDeleted())
        .appendText("\nEligibility").appendValue(expected.getEligibility());
  }

  @Override
  protected boolean matchesSafely(MobileClinicExportDonorViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId())
        && Objects.equals(actual.getDonorNumber(), expected.getDonorNumber())
        && Objects.equals(actual.getFirstName(), expected.getFirstName())
        && Objects.equals(actual.getLastName(), expected.getLastName())
        && Objects.equals(actual.getGender(), expected.getGender())
        && Objects.equals(actual.getBloodType(), expected.getBloodType())
        && Objects.equals(actual.getDonorStatus(), expected.getDonorStatus())
        && Objects.equals(actual.getBirthDate(), expected.getBirthDate())
        && Objects.equals(actual.getVenue(), expected.getVenue())
        && Objects.equals(actual.getIsDeleted(), expected.getIsDeleted())
        && Objects.equals(actual.getEligibility(), expected.getEligibility());
  }
  
  public static MobileClinicExportViewModelMatcher hasSameStateAsMobileClinicExportDonorViewModel(MobileClinicExportDonorViewModel expected) {
    return new MobileClinicExportViewModelMatcher(expected);
  }

}

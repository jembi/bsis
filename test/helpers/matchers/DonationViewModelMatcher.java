package helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import viewmodel.DonationViewModel;

public class DonationViewModelMatcher extends TypeSafeMatcher<DonationViewModel> {

  private DonationViewModel expected;

  public DonationViewModelMatcher(DonationViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A donation view model with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nPermissions: ").appendValue(expected.getPermissions())
        .appendText("\nAdverse Event: ").appendValue(expected.getAdverseEvent());
  }

  @Override
  public boolean matchesSafely(DonationViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getDonationDate(), expected.getDonationDate()) &&
        Objects.equals(actual.getDonationIdentificationNumber(), expected.getDonationIdentificationNumber()) &&
        Objects.equals(actual.getDonationType(), expected.getDonationType()) &&
        Objects.equals(actual.getPackType(), expected.getPackType()) &&
        Objects.equals(actual.getNotes(), expected.getNotes()) &&
        Objects.equals(actual.getDonorNumber(), expected.getDonorNumber()) &&
        Objects.equals(actual.getTTIStatus(), expected.getTTIStatus()) &&
        Objects.equals(actual.getPermissions(), expected.getPermissions()) &&
        Objects.equals(actual.getDonationBatchNumber(), expected.getDonationBatchNumber()) &&
        Objects.equals(actual.getBloodTypingStatus(), expected.getBloodTypingStatus()) &&
        Objects.equals(actual.getBloodTypingMatchStatus(), expected.getBloodTypingMatchStatus()) &&
        Objects.equals(actual.getBloodAbo(), expected.getBloodAbo()) &&
        Objects.equals(actual.getBloodRh(), expected.getBloodRh()) &&
        Objects.equals(actual.getHaemoglobinCount(), expected.getHaemoglobinCount()) &&
        Objects.equals(actual.getHaemoglobinLevel(), expected.getHaemoglobinLevel()) &&
        Objects.equals(actual.getDonorWeight(), expected.getDonorWeight()) &&
        Objects.equals(actual.getDonorPulse(), expected.getDonorPulse()) &&
        Objects.equals(actual.getBloodPressureSystolic(), expected.getBloodPressureSystolic()) &&
        Objects.equals(actual.getBloodPressureDiastolic(), expected.getBloodPressureDiastolic()) &&
        Objects.equals(actual.getBleedStartTime(), expected.getBleedStartTime()) &&
        Objects.equals(actual.getBleedEndTime(), expected.getBleedEndTime()) &&
        Objects.equals(actual.getVenue(), expected.getVenue()) &&
        Objects.equals(actual.isReleased(), expected.isReleased()) &&
        Objects.equals(actual.getPermissions(), expected.getPermissions()) &&
        Objects.equals(actual.getAdverseEvent(), expected.getAdverseEvent());
  }

  public static DonationViewModelMatcher hasSameStateAsDonationViewModel(DonationViewModel expected) {
    return new DonationViewModelMatcher(expected);
  }

}

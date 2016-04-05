package helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import viewmodel.DonationTestOutcomesReportViewModel;

public class DonationTestOutcomesReportViewModelMatcher extends TypeSafeMatcher<DonationTestOutcomesReportViewModel> {

  private DonationTestOutcomesReportViewModel expected;

  public DonationTestOutcomesReportViewModelMatcher(DonationTestOutcomesReportViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A test batch view model with the following state:")
        .appendText("\nBloodTypingStatus: ").appendValue(expected.getBloodTypingStatus())
        .appendText("\nTtiStatus: ").appendValue(expected.getTtiStatus())
        .appendText("\nDonationIdentificationNumber: ").appendValue(expected.getDonationIdentificationNumber())
        .appendText("\nPreviousDonationAboRhOutcome: ").appendValue(expected.getPreviousDonationAboRhOutcome())
        .appendText("\nBloodTestOutcomes: ").appendValue(expected.getBloodTestOutcomes());
        
  }

  @Override
  public boolean matchesSafely(DonationTestOutcomesReportViewModel actual) {
    return Objects.equals(actual.getBloodTypingStatus(), expected.getBloodTypingStatus())
        && Objects.equals(actual.getTtiStatus(), expected.getTtiStatus())
        && Objects.equals(actual.getDonationIdentificationNumber(), expected.getDonationIdentificationNumber())
        && Objects.equals(actual.getPreviousDonationAboRhOutcome(), expected.getPreviousDonationAboRhOutcome())
        && Objects.equals(actual.getBloodTestOutcomes(), expected.getBloodTestOutcomes());
  }

  public static DonationTestOutcomesReportViewModelMatcher hasSameStateAsDonationTestOutcomesReportViewModel(
      DonationTestOutcomesReportViewModel expected) {
    return new DonationTestOutcomesReportViewModelMatcher(expected);
  }

}

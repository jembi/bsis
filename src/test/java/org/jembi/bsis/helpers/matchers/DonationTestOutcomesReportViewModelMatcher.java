package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.DonationTestOutcomesReportViewModel;

public class DonationTestOutcomesReportViewModelMatcher extends AbstractTypeSafeMatcher<DonationTestOutcomesReportViewModel> {


  public DonationTestOutcomesReportViewModelMatcher(DonationTestOutcomesReportViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, DonationTestOutcomesReportViewModel model) {
    description.appendText("A DonationTestOutcomeReportViewModel with the following state:")
        .appendText("\nDonationIdentificationNumber: ").appendValue(model.getDonationIdentificationNumber())
        .appendText("\nBloodTypingStatus: ").appendValue(model.getBloodTypingStatus())
        .appendText("\nTtiStatus: ").appendValue(model.getTtiStatus())
        .appendText("\nPreviousDonationAboRhOutcome: ").appendValue(model.getPreviousDonationAboRhOutcome())
        .appendText("\nBloodTestOutcomes: ").appendValue(model.getBloodTestOutcomes());
        
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

package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.jembi.bsis.viewmodel.DonationViewModel;

public class DonationViewModelMatcher extends AbstractTypeSafeMatcher<DonationViewModel> {

  public DonationViewModelMatcher(DonationViewModel expected) {
    super(expected);
  }

  @Override
  public void appendDescription(Description description, DonationViewModel donationViewModel) {
    description.appendText("An DonationViewModel with the following state:")
    .appendText("\nId: ").appendValue(donationViewModel.getId())
    .appendText("\ndonationDate: ").appendValue(donationViewModel.getDonationDate())
    .appendText("\ndonationIdentificationNumber: ").appendValue(donationViewModel.getDonationIdentificationNumber())
    .appendText("\ndonationType: ").appendValue(donationViewModel.getDonationType())
    .appendText("\npackType: ").appendValue(donationViewModel.getPackType())
    .appendText("\ndonorNumber: ").appendValue(donationViewModel.getDonorNumber())
    .appendText("\nTTIStatus: ").appendValue(donationViewModel.getTTIStatus())
    .appendText("\nbloodTypingStatus: ").appendValue(donationViewModel.getBloodTypingStatus())
    .appendText("\nbloodTypingMatchStatus: ").appendValue(donationViewModel.getBloodTypingMatchStatus())
    .appendText("\nbloodABO: ").appendValue(donationViewModel.getBloodAbo())
    .appendText("\nbloodRh: ").appendValue(donationViewModel.getBloodRh())
    .appendText("\nvenue: ").appendValue(donationViewModel.getVenue())
    .appendText("\nreleased: ").appendValue(donationViewModel.isReleased());
  }

  @Override
  public boolean matchesSafely(DonationViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getDonationDate(), expected.getDonationDate()) &&
        Objects.equals(actual.getDonationIdentificationNumber(), expected.getDonationIdentificationNumber()) &&
        Objects.equals(actual.getDonationType(), expected.getDonationType()) &&
        Objects.equals(actual.getPackType(), expected.getPackType()) &&
        Objects.equals(actual.getDonorNumber(), expected.getDonorNumber()) &&
        Objects.equals(actual.getTTIStatus(), expected.getTTIStatus()) &&
        Objects.equals(actual.getBloodTypingStatus(), expected.getBloodTypingStatus()) &&
        Objects.equals(actual.getBloodTypingMatchStatus(), expected.getBloodTypingMatchStatus()) &&
        Objects.equals(actual.getBloodAbo(), expected.getBloodAbo()) &&
        Objects.equals(actual.getBloodRh(), expected.getBloodRh()) &&
        Objects.equals(actual.getVenue(), expected.getVenue()) &&
        Objects.equals(actual.isReleased(), expected.isReleased());
  }

  public static DonationViewModelMatcher hasSameStateAsDonationViewModel(DonationViewModel expected) {
    return new DonationViewModelMatcher(expected);
  }
}
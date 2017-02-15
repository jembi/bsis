package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.viewmodel.PostDonationCounsellingSummaryViewModel;


public class PostDonationCounsellingSummaryViewModelMatcher extends TypeSafeMatcher<PostDonationCounsellingSummaryViewModel> {

  private PostDonationCounsellingSummaryViewModel expected;

  public PostDonationCounsellingSummaryViewModelMatcher(PostDonationCounsellingSummaryViewModel expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A PostDonationCounsellingSummary view model with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nCounselled: ").appendValue(expected.getCounselled())
        .appendText("\nReferred: ").appendValue(expected.getReferred())
        .appendText("\nCounselling Date: ").appendValue(expected.getCounsellingDate())
        .appendText("\nDonor Number: ").appendValue(expected.getDonorNumber())
        .appendText("\nFirst Name: ").appendValue(expected.getFirstName())
        .appendText("\nLast Name: ").appendValue(expected.getLastName())
        .appendText("\nGender: ").appendValue(expected.getGender())
        .appendText("\nBirth Date: ").appendValue(expected.getBirthDate())
        .appendText("\nBloodGroup: ").appendValue(expected.getBloodGroup())
        .appendText("\nDonorId: ").appendValue(expected.getDonorId())
        .appendText("\nDIN: ").appendValue(expected.getDonationIdentificationNumber())
        .appendText("\nDonation Date: ").appendValue(expected.getDonationDate())
        .appendText("\nVenue: ").appendValue(expected.getVenue());
  }

  @Override
  public boolean matchesSafely(PostDonationCounsellingSummaryViewModel actual) {
    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.getCounselled(), expected.getCounselled()) &&
        Objects.equals(actual.getReferred(), expected.getReferred()) &&
        Objects.equals(actual.getCounsellingDate(), expected.getCounsellingDate()) &&
        Objects.equals(actual.getDonorNumber(), expected.getDonorNumber()) &&
        Objects.equals(actual.getFirstName(), expected.getFirstName()) &&
        Objects.equals(actual.getLastName(), expected.getLastName()) &&
        Objects.equals(actual.getGender(), expected.getGender()) &&
        Objects.equals(actual.getBirthDate(), expected.getBirthDate()) &&
        Objects.equals(actual.getBloodGroup(), expected.getBloodGroup()) &&
        Objects.equals(actual.getDonorId(), expected.getDonorId()) &&
        Objects.equals(actual.getDonationIdentificationNumber(), expected.getDonationIdentificationNumber()) &&
        Objects.equals(actual.getVenue(), expected.getVenue());
  }

  public static PostDonationCounsellingSummaryViewModelMatcher hasSameStateAsPostDonationCounsellingSummaryViewModel(PostDonationCounsellingSummaryViewModel expected) {
    return new PostDonationCounsellingSummaryViewModelMatcher(expected);
  }
}

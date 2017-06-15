package org.jembi.bsis.helpers.matchers;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.counselling.PostDonationCounselling;

public class PostDonationCounsellingMatcher extends TypeSafeMatcher<PostDonationCounselling> {

  private PostDonationCounselling expected;

  public PostDonationCounsellingMatcher(PostDonationCounselling expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A post donation counselling with the following state:")
        .appendText("\nId: ").appendValue(expected.getId())
        .appendText("\nFlagged for counselling: ").appendValue(expected.isFlaggedForCounselling())
        .appendText("\nDeleted: ").appendValue(expected.isIsDeleted())
        .appendText("\nCreated By: ").appendValue(expected.getCreatedBy())
        .appendText("\nCreated Date: ").appendValue(expected.getCreatedDate())
        .appendText("\nLast Updated By: ").appendValue(expected.getLastUpdatedBy())
        .appendText("\nLast Updated Date: ").appendValue(expected.getLastUpdated())
        .appendText("\nDonation: ").appendValue(expected.getDonation())
        .appendText("\nNotes: ").appendValue(expected.getNotes())
        .appendText("\nReferred: ").appendValue(expected.getReferred())
        .appendText("\nReferral Site: ").appendValue(expected.getReferralSite())
        .appendText("\nCounselling date: ").appendValue(expected.getCounsellingDate())
        .appendText("\nCounselling status: ").appendValue(expected.getCounsellingStatus());
  }

  @Override
  public boolean matchesSafely(PostDonationCounselling actual) {

    return Objects.equals(actual.getId(), expected.getId()) &&
        Objects.equals(actual.isFlaggedForCounselling(), expected.isFlaggedForCounselling()) &&
        Objects.equals(actual.isIsDeleted(), expected.isIsDeleted()) &&
        Objects.equals(actual.getCreatedBy(), expected.getCreatedBy()) &&
        Objects.equals(actual.getCreatedDate(), expected.getCreatedDate()) &&
        Objects.equals(actual.getLastUpdatedBy(), expected.getLastUpdatedBy()) &&
        Objects.equals(actual.getLastUpdated(), expected.getLastUpdated()) &&
        Objects.equals(actual.getDonation(), expected.getDonation()) &&
        Objects.equals(actual.getCounsellingDate(), expected.getCounsellingDate()) &&
        Objects.equals(actual.getNotes(), expected.getNotes()) &&
        Objects.equals(actual.getReferred(), expected.getReferred()) &&
        Objects.equals(actual.getReferralSite(), expected.getReferralSite()) &&
        Objects.equals(actual.getCounsellingStatus(), expected.getCounsellingStatus());
  }

  public static PostDonationCounsellingMatcher hasSameStateAsPostDonationCounselling(PostDonationCounselling expected) {
    return new PostDonationCounsellingMatcher(expected);
  }

}

package org.jembi.bsis.helpers.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;

public class DonationTestStatusesMatcher extends TypeSafeMatcher<Donation> {

  public static DonationTestStatusesMatcher testStatusesAreReset() {
    return new DonationTestStatusesMatcher();
  }

  @Override
  protected boolean matchesSafely(Donation donation) {
    return donation.getTTIStatus() == TTIStatus.NOT_DONE
        && donation.getBloodAbo() == null
        && donation.getBloodRh() == null
        && donation.getBloodTypingStatus() == BloodTypingStatus.NOT_DONE
        && donation.getBloodTypingMatchStatus() == BloodTypingMatchStatus.NOT_DONE;
  }

  @Override
  protected void describeMismatchSafely(Donation donation, Description description) {
    description.appendText("Donation: { ttiStatus: ").appendValue(donation.getTTIStatus())
        .appendText(", bloodAbo: ").appendValue(donation.getBloodAbo())
        .appendText(", bloodRh: ").appendValue(donation.getBloodRh())
        .appendText(", bloodTypingStatus: ").appendValue(donation.getBloodTypingStatus())
        .appendText(", bloodTypingMatchStatus: ").appendValue(donation.getBloodTypingMatchStatus())
        .appendText(" }");
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("Donation: { ttiStatus: ").appendValue(TTIStatus.NOT_DONE)
        .appendText(", bloodAbo: ").appendValue(null)
        .appendText(", bloodRh: ").appendValue(null)
        .appendText(", bloodTypingStatus: ").appendValue(BloodTypingStatus.NOT_DONE)
        .appendText(", bloodTypingMatchStatus: ").appendValue(BloodTypingMatchStatus.NOT_DONE)
        .appendText(" }");
  }
}

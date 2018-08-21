package org.jembi.bsis.model;

import org.jembi.bsis.model.donation.BloodTypingMatchStatus;
import org.jembi.bsis.model.donation.BloodTypingStatus;
import org.jembi.bsis.model.donation.Donation;
import org.jembi.bsis.model.donation.TTIStatus;
import org.jembi.bsis.suites.UnitTestSuite;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.jembi.bsis.helpers.builders.DonationBuilder.aDonation;
import static org.jembi.bsis.helpers.matchers.DonationTestStatusesMatcher.testStatusesAreReset;

public class DonationTests extends UnitTestSuite {

  @Test
  public void testResetTestStatuses_shouldSetStatusesToUntested() {
    Donation donation = aDonation()
        .withTTIStatus(TTIStatus.SAFE)
        .withBloodAbo("B")
        .withBloodRh("+")
        .withBloodTypingStatus(BloodTypingStatus.NOT_DONE)
        .withBloodTypingMatchStatus(BloodTypingMatchStatus.NOT_DONE)
        .build();

    donation.resetTestStatuses();

    assertThat(donation, testStatusesAreReset());
  }
}

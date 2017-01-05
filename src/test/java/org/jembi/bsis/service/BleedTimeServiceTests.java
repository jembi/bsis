package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Date;

import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;

public class BleedTimeServiceTests extends UnitTestSuite {

  @InjectMocks
  private BleedTimeService bleedTimeService;

  @Test
  public void testBleedTimeExceedingMax_shouldReturnTrue () {
    Date bleedStartTime = new DateTime().toDate();
    Date bleedEndTime = new DateTime().plusMinutes(35).toDate();
    int maxBleedTimeForComponent = 20;

    boolean markUnsafe = bleedTimeService.bleedTimeExceedsMax(bleedStartTime, bleedEndTime, maxBleedTimeForComponent);

    assertThat(markUnsafe, is(true));
  }

  @Test
  public void testBleedTimeNotExceedingMax_shouldReturnFalse () {
    Date bleedStartTime = new DateTime().toDate();
    Date bleedEndTime = new DateTime().plusMinutes(16).toDate();
    int maxBleedTimeForComponent = 20;

    boolean markUnsafe = bleedTimeService.bleedTimeExceedsMax(bleedStartTime, bleedEndTime, maxBleedTimeForComponent);

    assertThat(markUnsafe, is(false));
  }

  @Test
  public void testExceedingMaxTimeSinceDonation_shouldReturnTrue () {
    Date donationDate = new DateTime().minusHours(30).toDate();
    int maxTimeSinceDonationForComponent = 24;

    boolean markUnsafe = bleedTimeService.exceedsMaxTimeSinceDonation(donationDate, maxTimeSinceDonationForComponent);

    assertThat(markUnsafe, is(true));
  }

  @Test
  public void testNotExceedingMaxTimeSinceDonation_shouldReturnFalse () {
    Date donationDate = new DateTime().minusHours(20).toDate();
    int maxTimeSinceDonationForComponent = 30;

    boolean markUnsafe = bleedTimeService.exceedsMaxTimeSinceDonation(donationDate, maxTimeSinceDonationForComponent);

    assertThat(markUnsafe, is(false));
  }
}

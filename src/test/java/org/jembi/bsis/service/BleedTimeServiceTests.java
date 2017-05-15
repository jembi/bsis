package org.jembi.bsis.service;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;

import org.jembi.bsis.suites.UnitTestSuite;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;

public class BleedTimeServiceTests extends UnitTestSuite {

  @InjectMocks
  private BleedTimeService bleedTimeService;

  @Test
  public void testGetBleedTime_shouldReturnCorrectDifference () {
    // set up data
    Date bleedStartTime = new DateTime().toDate();
    Date bleedEndTime = new DateTime().plusMinutes(35).toDate();

    //run test
    Long bleedTime = bleedTimeService.getBleedTime(bleedStartTime, bleedEndTime);

    // do asserts
    assertThat("BleedTime is correct", bleedTime.equals(35L));
  }

  @Test
  public void testGetTimeSinceDonation_shouldReturnCorrectDifference () {
    // set up data
    Date donationDate = new DateTime().minusHours(30).toDate();
    Date processedOn = new DateTime().toDate();

    //run test
    Long timeSinceDonation = bleedTimeService.getTimeSinceDonation(donationDate, processedOn);
    
    //do asserts
    assertThat("Time since donation is correct", timeSinceDonation.equals(30L));
  }
}

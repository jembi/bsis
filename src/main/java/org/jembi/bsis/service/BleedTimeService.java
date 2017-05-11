package org.jembi.bsis.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

/**
 *Service for bleed time and donation time related business
 */
@Service
public class BleedTimeService {
  
  /**
   * Calculate bleed time from bleedStartTime and bleedEndTime in minutes
   *
   * @param startTime time when bleeding was started
   * @param endTime time when bleeding ended
   * @return the calculated bleed time with type long
   */
  public long getBleedTime(Date startTime, Date endTime) {
    return TimeUnit.MILLISECONDS.toMinutes(endTime.getTime() - startTime.getTime());
  }

  /**
   * Calculate elapsed time since donation in hours
   *
   * @param donationDate the date of donation
   * @param processedOn the component processing date
   * @return the calculated time Since donation with type long
   */
  public long getTimeSinceDonation(Date donationDate, Date processedOn) {
    return TimeUnit.MILLISECONDS.toHours(processedOn.getTime() - donationDate.getTime());
  }
}

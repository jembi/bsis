package org.jembi.bsis.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 *Service for bleed time and donation time related business
 */
@Service
public class BleedTimeService {
  private static final Logger LOGGER = Logger.getLogger(BleedTimeService.class);

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
    
    /**
     * FIXME:This code is a temporary fix and should be possibly removed at a later date
     */
    if (processedOn == null) {
      LOGGER.warn("Ignoring the time since donation check since the processedOn date is null");
    }
    return TimeUnit.MILLISECONDS.toHours(processedOn.getTime() - donationDate.getTime());
  }
}

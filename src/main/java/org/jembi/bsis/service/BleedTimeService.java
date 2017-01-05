package org.jembi.bsis.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class BleedTimeService {

  public long getBleedTime(Date startTime, Date endTime) {
    return TimeUnit.MILLISECONDS.toMinutes(endTime.getTime() - startTime.getTime());
  }

  public long getTimeSinceDonation(Date donationDate, Date processedOn) {
    return TimeUnit.MILLISECONDS.toHours(processedOn.getTime() - donationDate.getTime());
  }
}

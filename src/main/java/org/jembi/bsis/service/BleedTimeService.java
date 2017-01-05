package org.jembi.bsis.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class BleedTimeService {

  public boolean bleedTimeExceedsMax(Date startTime, Date endTime, int maxBleedTime) {
    long bleedTime = TimeUnit.MILLISECONDS.toMinutes(endTime.getTime() - startTime.getTime());
    if (bleedTime > maxBleedTime) {
      return true;
    }
    return false;
  }

  public boolean exceedsMaxTimeSinceDonation(Date donationDate, int maxTimeSinceDonation) {
    if(TimeUnit.MILLISECONDS.toHours(new Date().getTime() - donationDate.getTime()) > maxTimeSinceDonation) {
      return true;
    }
    return false;
  }
}

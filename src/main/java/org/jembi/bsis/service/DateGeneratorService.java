package org.jembi.bsis.service;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class DateGeneratorService {

  public Date generateDate() {
    return new Date();
  }

  /**
   * Takes the datePart day/month/year and the timePart hour:minute:second:millisecond and generates
   * a new date;
   *
   * @param datePart Date containing the final dd/MM/yyyy
   * @param timePart Date containing the final HH:mm:ss
   * @return Date new combined date
   */
  public Date generateDateTime(Date datePart, Date timePart) {
    Calendar sourceCal = Calendar.getInstance();
    sourceCal.setTime(datePart);
    Calendar destCal = Calendar.getInstance();
    destCal.setTime(timePart);

    destCal.set(Calendar.DAY_OF_MONTH, sourceCal.get(Calendar.DAY_OF_MONTH));
    destCal.set(Calendar.MONTH, sourceCal.get(Calendar.MONTH));
    destCal.set(Calendar.YEAR, sourceCal.get(Calendar.YEAR));

    return destCal.getTime();
  }

  /**
   * Returns a Date with the time part zeroed out to 00:00:00.
   * 
   * @param date
   * @return Date without the time part.
   */
  public Date generateDate(Date date) {
    return new LocalDate(date).toDate();
  }

  /**
   * Converts a java.util.Date to a LocalDate (Joda) 
   * @param date
   * @return
   */
  public LocalDate generateLocalDate(Date date) {
    if (date == null) {
      return null;
    }
    DateTime dt = new DateTime(date);
    return dt.toLocalDate();
  }

  /**
   * Returns new LocalDate with current date
   * 
   * @param date
   * @return
   */
  public LocalDate generateLocalDate() {
    return new LocalDate();
  }
}

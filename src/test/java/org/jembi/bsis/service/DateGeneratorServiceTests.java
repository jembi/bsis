package org.jembi.bsis.service;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DateGeneratorServiceTests {

  @InjectMocks
  private DateGeneratorService service;

  @Test
  public void testgenerateDateWithoutTimePart_shouldReturnDateWithTimeZero() throws Exception {
    Date date = new Date();

    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    Date expectedDate = cal.getTime();

    Date aDate = service.generateDate(date);

    assertEquals(aDate, expectedDate);

  }

  @Test
  public void testgenerateLocalDateWithNullDate_shouldReturnNull() throws Exception {
    LocalDate date = service.generateLocalDate(null);
    Assert.assertNull(date);
  }

  @Test
  public void testgenerateLocalDateWithCurrentDate_shouldReturnCurrentLocalDate() throws Exception {
    LocalDate date = new LocalDate();
    LocalDate localDate = service.generateLocalDate(date.toDate());
    Assert.assertEquals(localDate, date);
  }
}

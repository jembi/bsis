package org.jembi.bsis.service;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

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

    Date aDate = service.generateDateWithoutTimePart(date);

    assertEquals(aDate, expectedDate);

  }
}

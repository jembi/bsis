package org.jembi.bsis.helpers.matchers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class SameDayMatcher extends TypeSafeMatcher<Date> {
  
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  private Date expected;

  public SameDayMatcher(Date expected) {
    this.expected = expected;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("A date: ").appendValue(expected);
  }

  @Override
  protected boolean matchesSafely(Date actual) {
    String expectedDate = DATE_FORMAT.format(expected);
    String actualDate = DATE_FORMAT.format(actual);
    return Objects.equals(actualDate, expectedDate);
  }

  public static SameDayMatcher isSameDayAs(Date expected) {
    return new SameDayMatcher(expected);
  }
}

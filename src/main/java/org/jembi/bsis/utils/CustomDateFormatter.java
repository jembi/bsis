package org.jembi.bsis.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class CustomDateFormatter {
  
  private static final Logger LOGGER = Logger.getLogger(CustomDateFormatter.class);

  private static String datePattern = "yyyy-MM-dd";
  private static String dateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

  private static DateFormat dateFormat;
  private static DateFormat dateTimeFormat;

  static {
    dateFormat = new SimpleDateFormat(getDatePattern());
    dateFormat.setLenient(false);
    dateTimeFormat = new SimpleDateFormat(getDateTimePattern());
    dateTimeFormat.setLenient(false);
  }

  public static Date getDateFromString(String dateString) throws ParseException {
    Date date = null;
    if (!isDateEmpty(dateString)) {
      date = new LocalDate(dateString).toDate();
    }
    return date;
  }

  public static Date getDateTimeFromString(String dateTimeString) throws ParseException {
    Date date = null;
    if (!isDateEmpty(dateTimeString)) {
      date = new DateTime(dateTimeString).toDate();
    }
    return date;
  }

  public static boolean isDateEmpty(String dateString) {
    return (dateString == null || dateString.trim().isEmpty());
  }

  public static boolean isDateStringValid(String dateString) {
    boolean valid = false;
    if (isDateEmpty(dateString)) {
      valid = true;
    } else {
      try {
        dateFormat.parse(dateString);
        valid = true;
      } catch (ParseException ex) {
        LOGGER.error("Could not convert '" + dateString + "' to a date using format '" + dateFormat + "'", ex);
        valid = false;
      }
    }
    return valid;
  }

  public static boolean isDateTimeStringValid(String dateTimeString) {
    boolean valid = false;
    if (isDateEmpty(dateTimeString)) {
      valid = true;
    } else {
      try {
        dateTimeFormat.parse(dateTimeString);
        valid = true;
      } catch (ParseException ex) {
        LOGGER.error("Could not convert '" + dateTimeString + "' to a date using format '" + dateTimeFormat + "'", ex);
        valid = false;
      }
    }
    return valid;
  }

  public static String getDateErrorMessage() {
    return "Invalid Date specified.";
  }

  public static String getDateTimeErrorMessage() {
    return "Invalid Date specified.";
  }

  public static String getDateString(Date date) {
    if (date == null) {
      return "";
    } else {
      LocalDate localDate = new LocalDate(date);
      return localDate.toString();
    }
  }

  public static String getDateTimeString(Date date) {
    if (date == null) {
      return "";
    } else {
      return getISO8601StringForDate(date);
    }
  }

  public static String getTimeString(Date date) {
    if (date == null) {
      return "";
    } else {
      return getISO8601StringForDate(date);
    }
  }

  public static String getDatePattern() {
    return datePattern;
  }

  public static String getDateTimePattern() {
    return dateTimePattern;
  }

  public static String format(Date date) {
    return getDateTimeString(date);
  }

  public static Date parse(String dateStr) throws ParseException {
    return getDateTimeFromString(dateStr);
  }

  private static String getISO8601StringForDate(Date date) {
    DateFormat dateFormat = new SimpleDateFormat(dateTimePattern, Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    return dateFormat.format(date);
  }
}

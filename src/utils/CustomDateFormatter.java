package utils;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CustomDateFormatter {

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

    if (!isDateEmpty(dateString))
      date = new DateTime(dateString).toDate();

    return date;
  }

  public static Date getDateTimeFromString(String dateTimeString) throws ParseException {
    Date date = null;
    if (!isDateEmpty(dateTimeString))
      date = new DateTime(dateTimeString).toDate();
    return date;
  }

  private static boolean isDateEmpty(String dateString) {
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
        ex.printStackTrace();
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
        ex.printStackTrace();
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
    if (date == null)
      return "";
    else
      return getISO8601StringForDate(date);
  }

  public static String getDateTimeString(Date date) {
    if (date == null)
      return "";
    else
      return getISO8601StringForDate(date);
  }

  public static String getTimeString(Date date) {
    if (date == null)
      return "";
    else
      return getISO8601StringForDate(date);
  }

  private static String getDatePattern() {
    String datePattern = "yyyy-MM-dd";
    return datePattern;
  }

  private static String getDateTimePattern() {
    return dateTimePattern;
  }

  public static String format(Date date) {
    return getDateString(date);
  }

  public static Date parse(String dateStr) throws ParseException {
    return getDateFromString(dateStr);
  }

  private static String getISO8601StringForDate(Date date) {
    DateFormat dateFormat = new SimpleDateFormat(dateTimePattern, Locale.US);
    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    return dateFormat.format(date);
  }
}

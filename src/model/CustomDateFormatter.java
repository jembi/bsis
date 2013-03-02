package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateFormatter {

  public static final String datePattern = "MM/dd/yy"; 
  public static final String datePatternHumanReadable = "MM/dd/yy"; 

  public static final String dateTimePattern = "MM/dd/yy hh:mm:ss a"; 
  public static final String dateTimePatternHumanReadable = "MM/dd/yy hour:minute:second AM/PM";

  private static DateFormat dateFormat;
  private static DateFormat dateTimeFormat;

  static {
    dateFormat = new SimpleDateFormat(datePattern);
    dateFormat.setLenient(false);
    dateTimeFormat = new SimpleDateFormat(dateTimePattern);
    dateTimeFormat.setLenient(false);
  }

  public static Date getDateFromString(String dateString) throws ParseException {
    Date date = null;
    if (!isDateEmpty(dateString))
      date = dateFormat.parse(dateString);
    return date;
  }

  public static Date getDateTimeFromString(String dateTimeString) throws ParseException {
    Date date = null;
    if (!isDateEmpty(dateTimeString))
      date = dateTimeFormat.parse(dateTimeString);
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
    return "Invalid Date specified. Use " + datePatternHumanReadable.toLowerCase();
  }

  public static String getDateTimeErrorMessage() {
    return "Invalid Date specified. Use " + dateTimePatternHumanReadable.toLowerCase();
  }

  public static String getDateString(Date date) {
    if (date == null)
      return "";
    else
      return dateFormat.format(date);
  }

  public static String getDateTimeString(Date date) {
    if (date == null)
      return "";
    else
      return dateTimeFormat.format(date);
  }
}

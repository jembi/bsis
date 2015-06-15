package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateFormatter {

  private static String datePattern = "MM/dd/yyyy"; 
  private static String datePatternHumanReadable = "MM/dd/yyyy"; 

  private static String dateTimePattern = "MM/dd/yyyy hh:mm:ss a"; 
  private static String dateTimePatternHumanReadable = "MM/dd/yyyy hour:minute:second AM/PM";
  private static String timePattern = "hh:mm:ss a";
  private static String unixTimestampPattern = "EEE, dd MMM yyyy hh:mm:ss z";
  
  private static DateFormat dateFormat;
  private static DateFormat dateTimeFormat;
  private static DateFormat timeFormat;
  private static DateFormat unixTimestampFormat;

  static {
    dateFormat = new SimpleDateFormat(getDatePattern());
    dateFormat.setLenient(false);
    dateTimeFormat = new SimpleDateFormat(getDateTimePattern());
    dateTimeFormat.setLenient(false);
    timeFormat = new SimpleDateFormat(getTimePattern());
    timeFormat.setLenient(false);
    unixTimestampFormat = new SimpleDateFormat(getUnixTimestampPattern());
  }

  public static Date getDateFromString(String dateString) throws ParseException {
    Date date = null;
    if (!isDateEmpty(dateString))
      date = dateFormat.parse(dateString);
    return date;
  }

  public static Date getDateFromUnixTimestamp(Long unixSeconds) throws ParseException {
    return  new Date(unixSeconds*1000L);
  }

  public static Date getDateTimeFromString(String dateTimeString) throws ParseException {
    Date date = null;
    if (!isDateEmpty(dateTimeString))
      date = dateTimeFormat.parse(dateTimeString);
    return date;
  }
  
  public static Date getTimeFromString(String timeString) throws ParseException {
    Date date = null;
    if (!isDateEmpty(timeString))
      date = timeFormat.parse(timeString);
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
  
  public static boolean isTimeStringValid(String timeString) {
    boolean valid = false;
    if (isDateEmpty(timeString)) {
      valid = true;
    } else {
      try {
        timeFormat.parse(timeString);
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
  
  public static String getTimeErrorMessage() {
    return "Invalid Time specified.";
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

  public static String getUnixTimestampString(Date date) {
    if (date == null)
      return "";
    else
      return unixTimestampFormat.format(date);
  }

  public static long getUnixTimestampLong(Date date) {
    return date.getTime()/1000;
  }
  
  public static String getTimeString(Date date) {
    if (date == null)
      return "";
    else
      return timeFormat.format(date);
  }

  public static String getDatePattern() {
    return datePattern;
  }
  public static String getUnixTimestampPattern() {
    return unixTimestampPattern;
  }

  public static void setDatePattern(String datePattern) {
    CustomDateFormatter.datePattern = datePattern;
  }

  public static String getDatePatternHumanReadable() {
    return datePatternHumanReadable;
  }

  public static void setDatePatternHumanReadable(
      String datePatternHumanReadable) {
    CustomDateFormatter.datePatternHumanReadable = datePatternHumanReadable;
  }

  public static String getDateTimePattern() {
    return dateTimePattern;
  }

  public static String getDateTimePatternHumanReadable() {
    return dateTimePatternHumanReadable;
  }
  
  public static String getTimePattern() {
    return timePattern;
  }
  
  public static void setTimePattern(String timePattern) {
    CustomDateFormatter.timePattern = timePattern;
  }

  public static void setUnixTimestampPattern(String unixTimestampPattern) {
    CustomDateFormatter.unixTimestampPattern = unixTimestampPattern;
  }

  public static String format(Date date){
	  return getDateString(date);
  }
  
  public static Date parse(String dateStr) throws ParseException{
	  return getDateFromString(dateStr);
  }
}

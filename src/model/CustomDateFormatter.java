package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateFormatter {

  public static final String pattern = "MM/dd/yyyy"; 
  private static DateFormat format;

  static {
    format = new SimpleDateFormat(pattern);
    format.setLenient(false);
  }

  public static Date getDateFromString(String dateString) {
    Date date = null;
    try {
      if (!isDateEmpty(dateString))
        date = format.parse(dateString);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }
    System.out.println("here");
    System.out.println(date);
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
        format.parse(dateString);
        valid = true;
      } catch (ParseException ex) {
        ex.printStackTrace();
        valid = false;
      }
    }
    return valid;
  }

  public static String getErrorMessage() {
    return "Invalid Date specified. Use " + pattern.toLowerCase();
  }

  public static String getDateString(Date date) {
    if (date == null)
      return "";
    else
      return format.format(date);
  }
}

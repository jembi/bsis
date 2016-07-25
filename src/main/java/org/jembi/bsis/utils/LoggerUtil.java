package org.jembi.bsis.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jembi.bsis.log4j.BsisLogLevel;

public class LoggerUtil {
  static final Logger logger = Logger.getLogger("bsis");

  static {
    // PropertyConfigurator.configure("classes/log4j.properties");
  }

  public static void logUrl(HttpServletRequest request) {
    String urlString = request.getRequestURL().toString();
    String queryString = request.getQueryString();
    urlString += queryString != null && queryString.length() > 0 ? "?"
        + queryString : "";
    logger.log(BsisLogLevel.BSIS, urlString);
  }

  public static void setLogLevel(String level) {
    Level logLevel;
    
    if ("debug".equalsIgnoreCase(level)) {
      logLevel = Level.DEBUG;
    } else if ("info".equalsIgnoreCase(level)) {
      logLevel = Level.INFO;
    } else if ("warn".equalsIgnoreCase(level)) {
      logLevel = Level.WARN;
    } else if ("error".equalsIgnoreCase(level)) {
      logLevel = Level.ERROR;
    } else if ("fatal".equalsIgnoreCase(level)) {
      logLevel = Level.FATAL;
    } else {
      throw new IllegalArgumentException("Invalid log level: " + level);
    }

    // Set the BSIS log level
    LogManager.getLogger("org.jembi.bsis").setLevel(logLevel);
  }
}

package org.jembi.bsis.utils;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;

public class LoggerUtil {

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

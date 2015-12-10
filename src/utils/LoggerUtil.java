package utils;

import log4j.BsisLogLevel;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

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
    if ("debug".equalsIgnoreCase(level)) {
      LogManager.getRootLogger().setLevel(Level.DEBUG);
    } else if ("info".equalsIgnoreCase(level)) {
      LogManager.getRootLogger().setLevel(Level.INFO);
    } else if ("error".equalsIgnoreCase(level)) {
      LogManager.getRootLogger().setLevel(Level.ERROR);
    } else if ("fatal".equalsIgnoreCase(level)) {
      LogManager.getRootLogger().setLevel(Level.FATAL);
    } else if ("warn".equalsIgnoreCase(level)) {
      LogManager.getRootLogger().setLevel(Level.WARN);
    } else {
      // Default to info log level
      LogManager.getRootLogger().setLevel(Level.INFO);
    }
  }
}

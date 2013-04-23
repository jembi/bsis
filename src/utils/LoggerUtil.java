package utils;

import javax.servlet.http.HttpServletRequest;

import log4j.V2VLogLevel;

import org.apache.log4j.Logger;

public class LoggerUtil {
	static final Logger logger = Logger.getLogger("v2v");

	static {
		// PropertyConfigurator.configure("classes/log4j.properties");
	}

	public static void logUrl(HttpServletRequest request) {
		String urlString = request.getRequestURL().toString();
		String queryString = request.getQueryString();
		urlString += queryString != null && queryString.length() > 0 ? "?"
				+ queryString : "";
		logger.log(V2VLogLevel.V2V, urlString);
	}
}

package org.jembi.bsis.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoggingInterceptor extends HandlerInterceptorAdapter {

  private static final Logger LOGGER = Logger.getLogger(LoggingInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    if (LOGGER.isDebugEnabled()) {
      String urlString = request.getRequestURL().toString();
      String queryString = request.getQueryString();
      if (StringUtils.isNotBlank(queryString)) {
        urlString += "?" + queryString;
      }
      LOGGER.debug(urlString);
    }
    return super.preHandle(request, response, handler);
  }
}

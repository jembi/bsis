package org.jembi.bsis.repository.events;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Based on what I found here http://vishwanathk.wordpress.com/2011/06/15/how-to-publish-and-subscribe-to-events-with-spring/
 * http://mythinkpond.wordpress.com/2010/03/22/spring-application-context/
 *
 * @author iamrohitbanga
 */
public class ApplicationContextProvider implements ApplicationContextAware {

  private static ApplicationContext applicationContext = null;

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public void setApplicationContext(ApplicationContext ctx) throws BeansException {
    this.applicationContext = ctx;
  }
}

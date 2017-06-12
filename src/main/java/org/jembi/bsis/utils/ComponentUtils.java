package org.jembi.bsis.utils;

import java.util.Date;

import org.jembi.bsis.model.component.Component;
import org.joda.time.DateTime;
import org.joda.time.Days;

public class ComponentUtils {
  
  public static int getDaysToExpire(Component component){
    
    Date today = new Date();
    if(today.equals(component.getExpiresOn()) || today.before(component.getExpiresOn())) {
      DateTime expiresOn = new DateTime(component.getExpiresOn().getTime());
      int daysToExpire = Days.daysBetween(new DateTime(), expiresOn).getDays();
      return daysToExpire;
      } 
    else {
      return 0;
      }
    }
  }

package org.jembi.bsis.util;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Subclass of java.util.Date that generates a random date in the last 30 days.
 * 
 * Should be used in the Builders instead of new Date() to avoid non-deterministic
 * tests. 
 * 
 * The problem with using "new Date()" as a default in the builder is that
 * if the date is required to compare two objects created by the builder, it will 
 * sometimes be the same and sometimes not.
 */
public class RandomTestDate extends Date {

  private static final long serialVersionUID = 6286636523146859521L;
  private static final ThreadLocalRandom RAND = ThreadLocalRandom.current();
  private static final long THIRTY_DAYS_IN_MILLISECONDS = 1000l*60l*60l*24l*30l;
  private static final long THIRTY_DAYS_AGO = System.currentTimeMillis() - THIRTY_DAYS_IN_MILLISECONDS;

  public RandomTestDate() {
    super(THIRTY_DAYS_AGO + RAND.nextLong(THIRTY_DAYS_IN_MILLISECONDS));
  }
}
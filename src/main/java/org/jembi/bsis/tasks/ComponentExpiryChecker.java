package org.jembi.bsis.tasks;

import org.apache.log4j.Logger;
import org.jembi.bsis.repository.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ComponentExpiryChecker {
  
  private static final Logger LOGGER = Logger.getLogger(ComponentExpiryChecker.class);

  @Autowired
  ComponentRepository componentRepository;

  public ComponentExpiryChecker() {
  }

  @Scheduled(fixedDelay = 30 * 60 * 1000)
  public void run() {
    LOGGER.trace("Updating Component Expiry Status");
    long t1 = System.currentTimeMillis();
    
    componentRepository.updateExpiryStatus();
    
    long t2 = System.currentTimeMillis();
    double timeTaken = (t2 - t1) / 1000.0;
    LOGGER.trace("Time taken: " + timeTaken + " seconds");
  }

}

package tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import repository.ComponentRepository;

@Component
public class ComponentExpiryChecker {

  @Autowired
  ComponentRepository componentRepository;

  public ComponentExpiryChecker() {
  }

  @Scheduled(fixedDelay = 30 * 60 * 1000)
  public void run() {
    //System.out.println("Updating Component Expiry Status");
    long t1 = System.currentTimeMillis();
    componentRepository.updateExpiryStatus();
    long t2 = System.currentTimeMillis();
    double timeTaken = (t2 - t1) / 1000.0;
    //System.out.println("Time taken: " + timeTaken + " seconds");
  }

}

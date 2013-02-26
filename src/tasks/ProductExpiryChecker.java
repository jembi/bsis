package tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import repository.ProductRepository;

@Component
public class ProductExpiryChecker {

  @Autowired
  ProductRepository productRepository;

  public ProductExpiryChecker() {
  }

  @Scheduled(fixedDelay=10*60*1000)
  public void run() {
    System.out.println("Updating Product Expiry Status");
    long t1 = System.currentTimeMillis();
    productRepository.updateExpiryStatus();
    long t2 = System.currentTimeMillis();
    double timeTaken = (t2-t1) / 1000.0;
    System.out.println("Time taken: " + timeTaken + " seconds");
  }

}

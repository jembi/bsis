package tasks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import repository.ComponentRepository;

@Component
public class ProductQuarantineStatusUpdater implements Runnable {

  @Autowired
  ComponentRepository componentRepository;

  public ProductQuarantineStatusUpdater() {
  }

  @Scheduled(fixedDelay=2*3600*1000)
  public void run() {
    componentRepository.updateQuarantineStatus();
  }

}

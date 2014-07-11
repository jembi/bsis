/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package repository.listeners;

/**
 *
 * @author srikanth
 */
import model.donationbatch.DonationBatchSession;
import model.donationbatch.DonationBatchSessionSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import repository.DonationBatchRepository;

@Component
public class DonationBatchContextListener implements ApplicationListener<ContextRefreshedEvent> {

  @Autowired
  private DonationBatchRepository donationBatchRepository;
    
  @Override
  public void onApplicationEvent(final ContextRefreshedEvent event) {
          DonationBatchSessionSingleton batchSessionSingleton = null;
          
          DonationBatchSession donationBatchSession = donationBatchRepository.getCurrenrDonationBatchSession();
          
          if (donationBatchSession != null) {
              batchSessionSingleton = DonationBatchSessionSingleton.getInstance();
              batchSessionSingleton.setDonationBatch(donationBatchSession.getDonationBatch());
          }
          
   }
  
}
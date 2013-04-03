package repository.listeners;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.collectedsample.CollectedSample;
import model.donor.Donor;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import repository.events.CollectionAddedEvent;

@Repository
@Transactional
public class CollectionAddedEventListener implements ApplicationListener<CollectionAddedEvent> {

  @PersistenceContext
  private EntityManager em;
  
  @Override
  public void onApplicationEvent(CollectionAddedEvent event) {
    System.out.println("collection added event listener called");
    System.out.println("event ID: " + event.getEventId());
    System.out.println("event context: " + event.getEventContext());
    updateDonor(event);
  }

  private void updateDonor(CollectionAddedEvent event) {
    CollectedSample c = (CollectedSample) event.getEventContext();
    Donor donor = c.getDonor();
    if (donor == null)
      return;
//    donor.setDateOfLastDonation(c.getCollectedOn());
    em.merge(donor);
  }

}

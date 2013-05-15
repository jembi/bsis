package repository.listeners;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.collectedsample.CollectedSample;
import model.donor.Donor;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import repository.events.CollectionUpdatedEvent;

@Repository
@Transactional
public class CollectionUpdatedEventListener implements ApplicationListener<CollectionUpdatedEvent> {

  @PersistenceContext
  private EntityManager em;
  
  @Override
  public void onApplicationEvent(CollectionUpdatedEvent event) {
    updateDonor(event);
  }

  private void updateDonor(CollectionUpdatedEvent event) {
    CollectedSample c = (CollectedSample) event.getEventContext();
    Donor donor = c.getDonor();
    if (donor == null)
      return;
    Date dateOfLastDonation = donor.getDateOfLastDonation();
    if (dateOfLastDonation == null || c.getCollectedOn().after(dateOfLastDonation)) {
      donor.setDateOfLastDonation(c.getCollectedOn());
    }
    em.merge(donor);
  }

}

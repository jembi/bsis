package helpers.persisters;

import static helpers.persisters.EntityPersisterFactory.aComponentBatchPersister;
import static helpers.persisters.EntityPersisterFactory.aLocationPersister;

import javax.persistence.EntityManager;

import model.donationbatch.DonationBatch;

public class DonationBatchPersister extends AbstractEntityPersister<DonationBatch> {

  @Override
  public DonationBatch deepPersist(DonationBatch donationBatch, EntityManager entityManager) {
    if (donationBatch.getVenue() != null) {
      aLocationPersister().deepPersist(donationBatch.getVenue(), entityManager);
    }
    
    if (donationBatch.getComponentBatch() != null) {
      aComponentBatchPersister().deepPersist(donationBatch.getComponentBatch(), entityManager);
    }
    return persist(donationBatch, entityManager);
  }

}

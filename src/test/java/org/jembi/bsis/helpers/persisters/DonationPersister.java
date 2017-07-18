package org.jembi.bsis.helpers.persisters;

import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aDonationBatchPersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aDonationTypePersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aDonorPersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aLocationPersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aPackTypePersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aTestBatchPersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aUserPersister;
import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.anAdverseEventPersister;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.donation.Donation;

public class DonationPersister extends AbstractEntityPersister<Donation> {

  @Override
  public Donation deepPersist(Donation donation, EntityManager entityManager) {
    if (donation.getDonor() != null) {
      aDonorPersister().deepPersist(donation.getDonor(), entityManager);
    }

    if (donation.getVenue() != null) {
      aLocationPersister().deepPersist(donation.getVenue(), entityManager);
    }

    if (donation.getAdverseEvent() != null) {
      anAdverseEventPersister().deepPersist(donation.getAdverseEvent(), entityManager);
    }

    if (donation.getDonationType() != null) {
      aDonationTypePersister().deepPersist(donation.getDonationType(), entityManager);
    }

    if (donation.getPackType() != null) {
      aPackTypePersister().deepPersist(donation.getPackType(), entityManager);
    }

    if (donation.getDonationBatch() != null) {
      aDonationBatchPersister().deepPersist(donation.getDonationBatch(), entityManager);
    }
    
    if (donation.getCreatedBy() != null) {
      aUserPersister().deepPersist(donation.getCreatedBy(), entityManager);
    }

    if (donation.getTestBatch() != null) {
      aTestBatchPersister().deepPersist(donation.getTestBatch(), entityManager);
    }

    return persist(donation, entityManager);
  }

}

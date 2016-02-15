package helpers.persisters;

import static helpers.persisters.EntityPersisterFactory.aDonationTypePersister;
import static helpers.persisters.EntityPersisterFactory.aDonorPersister;
import static helpers.persisters.EntityPersisterFactory.aLocationPersister;
import static helpers.persisters.EntityPersisterFactory.aPackTypePersister;
import static helpers.persisters.EntityPersisterFactory.anAdverseEventPersister;

import javax.persistence.EntityManager;

import model.donation.Donation;

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

    return persist(donation, entityManager);
  }

}

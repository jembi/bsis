package helpers.persisters;

import model.donation.Donation;

import javax.persistence.EntityManager;

import static helpers.persisters.EntityPersisterFactory.*;

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

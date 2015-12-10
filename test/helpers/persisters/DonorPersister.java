package helpers.persisters;

import model.donor.Donor;

import javax.persistence.EntityManager;

import static helpers.persisters.EntityPersisterFactory.aLocationPersister;

public class DonorPersister extends AbstractEntityPersister<Donor> {

  @Override
  public Donor deepPersist(Donor donor, EntityManager entityManager) {

    if (donor.getVenue() != null) {
      aLocationPersister().deepPersist(donor.getVenue(), entityManager);
    }

    return persist(donor, entityManager);
  }

}

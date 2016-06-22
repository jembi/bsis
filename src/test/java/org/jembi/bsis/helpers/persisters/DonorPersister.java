package org.jembi.bsis.helpers.persisters;

import static org.jembi.bsis.helpers.persisters.EntityPersisterFactory.aLocationPersister;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.donor.Donor;

public class DonorPersister extends AbstractEntityPersister<Donor> {

  @Override
  public Donor deepPersist(Donor donor, EntityManager entityManager) {

    if (donor.getVenue() != null) {
      aLocationPersister().deepPersist(donor.getVenue(), entityManager);
    }

    return persist(donor, entityManager);
  }

}

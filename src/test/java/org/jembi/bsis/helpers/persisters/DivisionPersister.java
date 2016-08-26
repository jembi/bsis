package org.jembi.bsis.helpers.persisters;

import javax.persistence.EntityManager;

import org.jembi.bsis.model.location.Division;

public class DivisionPersister extends AbstractEntityPersister<Division> {

  @Override
  public Division deepPersist(Division division, EntityManager entityManager) {
    if (division.getParent() != null) {
      deepPersist(division.getParent(), entityManager);
    }
    return persist(division, entityManager);
  }

}

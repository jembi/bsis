package org.jembi.bsis.helpers.persisters;

import javax.persistence.EntityManager;

public class DefaultPersister<T> extends AbstractEntityPersister<T> {

  @Override
  public T deepPersist(T entity, EntityManager entityManager) {
    return persist(entity, entityManager);
  }

}

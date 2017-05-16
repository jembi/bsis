package org.jembi.bsis.helpers.persisters;

import javax.persistence.EntityManager;

public abstract class AbstractEntityPersister<T> {

  public abstract T deepPersist(T entity, EntityManager entityManager);

  public T persist(T entity, EntityManager entityManager) {
    entityManager.persist(entity);
    entityManager.flush();
    return entity;
  }

}

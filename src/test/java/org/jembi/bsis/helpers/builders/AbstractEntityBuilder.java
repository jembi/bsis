package org.jembi.bsis.helpers.builders;

import javax.persistence.EntityManager;

import org.jembi.bsis.helpers.persisters.AbstractEntityPersister;
import org.jembi.bsis.helpers.persisters.DefaultPersister;
import org.jembi.bsis.model.BSISEntity;

public abstract class AbstractEntityBuilder<T extends BSISEntity> extends AbstractBuilder<T> {

  public AbstractEntityPersister<T> getPersister() {
    return new DefaultPersister<>();
  }

  public T buildAndPersist(EntityManager entityManager) {
    T entity = build();
    return getPersister().deepPersist(entity, entityManager);
  }

}
